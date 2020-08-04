package com.ktkj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@RestController
@RequestMapping("/wechat/security")
public class SecurityController {

    /**
     * 获取用户的收货地址
     */
    @RequestMapping(value = "/security", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public Object security(@RequestParam Map<String, Object> params) {

        return params.get("echostr");
    }
}