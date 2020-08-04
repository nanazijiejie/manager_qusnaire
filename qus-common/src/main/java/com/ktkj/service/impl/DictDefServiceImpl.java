/*
 * 类名称:DictDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.dao.DictDefDao;
import com.ktkj.entity.DictDefEntity;
import com.ktkj.service.DictDefService;
import com.ktkj.utils.Constant;
import com.ktkj.utils.QueryPlus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Service("dictDefService")
public class DictDefServiceImpl extends ServiceImpl<DictDefDao, DictDefEntity> implements DictDefService {
    @Override
    public List<DictDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public String qryItemName(String typeCode, String itemValue) {
        List<DictDefEntity> dictList = null;
        if (J2CacheUtils.get(Constant.DATA_DICT) != null) {
            dictList = (List<DictDefEntity>) J2CacheUtils.get(Constant.DATA_DICT);
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            dictList = this.queryAll(params);
            J2CacheUtils.set(Constant.DATA_DICT, dictList, Constant.ALIVE_SECONDS, false);
        }
        for (DictDefEntity dictDefEntity : dictList
                ) {
            if (typeCode.equals(dictDefEntity.getTypeCode())
                    && itemValue.equals(dictDefEntity.getItemValue())) {
                return dictDefEntity.getItemName();
            }
        }
        return "";

    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.code");
        params.put("asc", false);
        Page<DictDefEntity> page = new QueryPlus<DictDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectDictDefPage(page, params));
    }

    @Override
    public boolean add(DictDefEntity dictDef) {
        boolean result = this.save(dictDef);
        J2CacheUtils.set(Constant.DATA_DICT, this.queryAll(new HashMap<>(0)), Constant.ALIVE_SECONDS, false);
        return result;
    }

    @Override
    public boolean update(DictDefEntity dictDef) {
        boolean result = this.updateById(dictDef);
        J2CacheUtils.set(Constant.DATA_DICT, this.queryAll(new HashMap<>(0)), Constant.ALIVE_SECONDS, false);
        return result;
    }

    @Override
    public boolean delete(Integer code) {
        boolean result = this.removeById(code);
        J2CacheUtils.set(Constant.DATA_DICT, this.queryAll(new HashMap<>(0)), Constant.ALIVE_SECONDS, false);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] codes) {
        boolean result = this.removeByIds(Arrays.asList(codes));
        J2CacheUtils.set(Constant.DATA_DICT, this.queryAll(new HashMap<>(0)), Constant.ALIVE_SECONDS, false);
        return result;
    }
}
