package com.ktkj.controller;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ktkj.util.JsSignUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weixinscan")
public class WeixinScanController {
    /**
     * 微信扫一扫接口的后台处理程序
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object weixinScan( HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        String weburl = request.getParameter("url");

        Map<String, String> resMap = new HashMap<String, String>();
        resMap = JsSignUtil.sign(weburl);

        return resMap;
    }
}
