package com.ktkj.api;

import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.annotation.LoginUser;
import com.ktkj.entity.UserVo;
import com.ktkj.service.ApiUserService;
import com.ktkj.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API测试接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:47
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class ApiTestController {

    @Autowired
    private ApiUserService userService;

    /**
     * 获取用户信息
     */
    @PostMapping("userInfo")
    public R userInfo(@LoginUser UserVo user) {
        return R.ok().put("user", user);
    }

    /**
     * 忽略Token验证测试
     */
    @IgnoreAuth
    @PostMapping("notToken")
    public R notToken() {
        return R.ok().put("msg", "无需token也能访问。。。");
    }

    /**
     * 根据手机号查询用户信息接口测试方法
     *
     * @param mobile
     * @return
     */
    @IgnoreAuth
    @PostMapping("userListByMobile")
    public R userList(String mobile) {
        UserVo userEntity = userService.queryByMobile(mobile);
        return R.ok().put("dto", userEntity);
    }
}
