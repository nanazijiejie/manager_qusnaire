package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.dao.SysOssDao;
import com.ktkj.entity.SysOssEntity;
import com.ktkj.service.SysOssService;
import com.ktkj.utils.PageUtilsPlus;
import com.ktkj.utils.QueryPlus;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 李鹏军
 */
@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Override
    public PageUtilsPlus queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "t.create_date");
        params.put("asc", false);
        Page<SysOssEntity> page = new QueryPlus<SysOssEntity>(params).getPage();
        return new PageUtilsPlus(page.setRecords(baseMapper.selectSysOssPage(page, params)));
    }
}
