package com.ktkj.api;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.entity.FullUserInfo;
import com.ktkj.entity.UserInfo;
import com.ktkj.entity.UserVo;
import com.ktkj.service.ApiUserService;
import com.ktkj.service.TokenService;
import com.ktkj.util.ApiBaseAction;
import com.ktkj.util.ApiUserUtils;
import com.ktkj.util.CommonUtil;
import com.ktkj.utils.CharUtil;
import com.ktkj.utils.R;
import com.ktkj.utils.ResourceUtil;
import com.ktkj.validator.Assert;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * API登录授权
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:31
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public R login(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        //用户登录
        long userId = userService.login(mobile, password);

        //生成token
        Map<String, Object> map = tokenService.createToken(userId);

        return R.ok(map);
    }

    /**
     * 微信登录
     */
    @ApiOperation(value = "微信登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin() {
        JSONObject jsonParam = this.getJsonRequest();
        FullUserInfo fullUserInfo = null;
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        if (null != jsonParam.get("userInfo")) {
            fullUserInfo = jsonParam.getObject("userInfo", FullUserInfo.class);
        }
        if (null == fullUserInfo) {
            return toResponsFail("登录失败");
        }

        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        UserInfo userInfo = fullUserInfo.getUserInfo();

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》组合token为：" + requestUrl);
        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null == sessionData || StringUtils.isNullOrEmpty(sessionData.getString("openid"))) {
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性
        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
        if (!fullUserInfo.getSignature().equals(sha1)) {
            return toResponsFail("登录失败");
        }
        Date nowTime = new Date();
        UserVo userVo = userService.queryByOpenId(sessionData.getString("openid"));
        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUsername("微信用户" + CharUtil.getRandomString(12));
            userVo.setPassword(sessionData.getString("openid"));
            userVo.setRegister_time(nowTime);
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            //性别 0：未知、1：男、2：女
            userVo.setGender(userInfo.getGender());
            userVo.setNickname(userInfo.getNickName());
            userService.save(userVo);
        } else {
            userVo.setLast_login_ip(this.getClientIp());
            userVo.setLast_login_time(nowTime);
            userService.update(userVo);
        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (null == userInfo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        resultObj.put("token", token);
        resultObj.put("userInfo", userInfo);
        resultObj.put("userId", userVo.getUserId());
        return toResponsSuccess(resultObj);
    }

    /**
     * 支付宝登录
     */
    @ApiOperation(value = "支付宝登录")
    @IgnoreAuth
    @PostMapping("login_by_ali")
    public Object login_by_ali() {
        JSONObject jsonParam = this.getJsonRequest();
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(ResourceUtil.getConfigByName("ali.webAccessTokenhttps"), ResourceUtil.getConfigByName("ali.appId"), ResourceUtil.getConfigByName("ali.privateKey"),
                "json", "UTF-8", ResourceUtil.getConfigByName("ali.pubKey"), "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        try {
            //code 换取token
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            String accessToken = oauthTokenResponse.getAccessToken();

            //根据token获取用户头像、昵称等信息
            AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse userInfoResponse = alipayClient.execute(userInfoShareRequest, accessToken);

            Date nowTime = new Date();
            UserVo userVo = userService.queryByOpenId(userInfoResponse.getUserId());
            if (null == userVo) {
                userVo = new UserVo();
                userVo.setUsername("支付宝用户" + CharUtil.getRandomString(12));
                userVo.setPassword(userInfoResponse.getUserId());
                userVo.setRegister_time(nowTime);
                userVo.setRegister_ip(this.getClientIp());
                userVo.setLast_login_ip(userVo.getRegister_ip());
                userVo.setLast_login_time(nowTime);
                userVo.setWeixin_openid(userInfoResponse.getUserId());
                userVo.setAvatar(userInfoResponse.getAvatar());
                //性别 0：未知、1：男、2：女
                //F：女性；M：男性
                userVo.setGender("m".equalsIgnoreCase(userInfoResponse.getGender()) ? 1 : 0);
                userVo.setNickname(userInfoResponse.getNickName());
                userService.save(userVo);
            } else {
                userVo.setLast_login_ip(this.getClientIp());
                userVo.setLast_login_time(nowTime);
                userService.update(userVo);
            }

            Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
            String token = MapUtils.getString(tokenMap, "token");

            if (StringUtils.isNullOrEmpty(token)) {
                return toResponsFail("登录失败");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();
            resultObj.put("token", token);
            resultObj.put("userInfo", userInfoResponse);
            resultObj.put("userId", userVo.getUserId());
            return toResponsSuccess(resultObj);
        } catch (AlipayApiException e) {
            return toResponsFail("登录失败");
        }
    }

    /**
     * 微信授权，获取getCodeUrl
     */
    @ApiIgnore
    @IgnoreAuth
    @RequestMapping(value = "/getCodeUrl", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public void getCodeUrl(HttpServletResponse response) {
        String state = "1";
        String getCodeUrl = ApiUserUtils.getCode(state, "https://admin.jixuxiang.com/ring/api/auth/oAuth_weixin", "snsapi_base");
        try {
            response.sendRedirect(getCodeUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信授权，回调地址
     */
    @ApiIgnore
    @IgnoreAuth
    @RequestMapping(value = "/oAuth_weixin", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public void oAuth_weixin(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        try {
            // 第二步：通过code换取网页授权access_token
            String getOAuthAccessTokenUrl = ApiUserUtils.getOAuthAccessToken(code);
            JSONObject getOAuthAccessTokenSessionData = CommonUtil.httpsRequest(getOAuthAccessTokenUrl, "POST", null);

            if (null == getOAuthAccessTokenSessionData || StringUtils.isNullOrEmpty(getOAuthAccessTokenSessionData.getString("access_token"))) {
                logger.error("微信授权失败，请联系系统管理员！");
            }

            // 第三步：刷新access_token（如果需要）
            String accessToken = getOAuthAccessTokenSessionData.getString("access_token");
            // TODO 保存 access_token、refresh_token

            if (null == getOAuthAccessTokenSessionData || StringUtils.isNullOrEmpty(getOAuthAccessTokenSessionData.getString("openid"))) {
                logger.error("微信授权失败，请联系系统管理员！");
            }

            String openId = getOAuthAccessTokenSessionData.getString("openid");

            // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
            String getUserInfoByOpenIdUrl = ApiUserUtils.getUserInfoByOpenId(accessToken, openId);
            JSONObject getUserInfoByOpenIdSessionData = CommonUtil.httpsRequest(getUserInfoByOpenIdUrl, "GET", null);

            if (null == getUserInfoByOpenIdSessionData || StringUtils.isNullOrEmpty(getUserInfoByOpenIdSessionData.getString("openid"))) {
                logger.error("微信授权失败，请联系系统管理员！");
            }

            String nickName = getUserInfoByOpenIdSessionData.getString("nickname");

//            String openId = "123123123";
//            request.setAttribute("openId",openId);
            response.sendRedirect("https://admin.jixuxiang.com/ring/html/ring/orderInfo.html?openId=" + openId);
//            redirectAttributes.addAttribute("openId", openId);
//            ModelAndView mv = new ModelAndView("redirect:http://localhost:8080/ring/html/ring/orderInfo.html");
//            mv.addObject("openId", openId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
