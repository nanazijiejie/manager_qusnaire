package com.ktkj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.SysOssEntity;
import com.ktkj.utils.PageUtilsPlus;

import java.util.Map;

/**
 * 文件上传Service
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-25 12:13:26
 */
public interface SysOssService extends IService<SysOssEntity> {

    /**
     * queryPage
     *
     * @param params
     * @return
     */
    PageUtilsPlus queryPage(Map<String, Object> params);
}
