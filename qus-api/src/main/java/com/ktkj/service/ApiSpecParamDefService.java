/*
 * 类名称:SpecParamDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.ktkj.dao.ApiSpecParamDefMapper;
import com.ktkj.entity.SpecParamDefVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Service("apiSpecParamDefService")
public class ApiSpecParamDefService {
    @Autowired
    private ApiSpecParamDefMapper apiSpecParamDefDao;

    public List<SpecParamDefVo> queryAll(Map<String, Object> params) {
        return apiSpecParamDefDao.queryAll(params);
    }

}
