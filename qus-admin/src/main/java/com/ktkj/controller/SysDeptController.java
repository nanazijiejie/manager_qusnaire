package com.ktkj.controller;

import com.ktkj.annotation.SysLog;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.SysDeptEntity;
import com.ktkj.service.DictDefService;
import com.ktkj.service.SysDeptService;
import com.ktkj.utils.Constant;
import com.ktkj.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理Controller
 *
 * @author liepngjun
 * @email 939961241@qq.com
 * @date 2017-09-17 23:58:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private DictDefService dictDefService;

    /**
     * 部门列表
     *
     * @return R
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dept:list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只能查询本部门及子部门数据
        if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {//不是超级管理员，只能查看本地市的数据
            //map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
            map.put("cityFilter", getUser().getCityId());
        }else{
            map.put("cityFilter", params.get("cityId"));
        }
        List<SysDeptEntity> deptList = sysDeptService.queryList(map);
        return R.ok().put("list", deptList);
    }

    /**
     * 选择部门(添加、修改菜单)
     *
     * @return R
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只能查询本部门及子部门数据
        if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {
            map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
        }
        List<SysDeptEntity> deptList = sysDeptService.queryList(map);

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN||"Z".equals(getUser().getCityId())) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 获取用户部门Id(管理员则为0)
     *
     * @return
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:dept:list")
    public R info() {
        long deptId = 0;
        if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {
            SysDeptEntity dept = sysDeptService.queryObject(getDeptId());
            deptId = dept.getParentId();
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 根据主键获取部门信息
     *
     * @param deptId 主键
     * @return R
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.queryObject(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 新增部门
     *
     * @param dept 部门
     * @return R
     */
    @SysLog("新增部门")
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(@RequestBody SysDeptEntity dept) {
        if(getUserId()==Constant.SUPER_ADMIN||"Z".equals(getUser().getCityId())){//管理员
            dept.setCity(dictDefService.qryItemName("CITY",dept.getCityId()));
        }else{
            String cityId = getUser().getCityId();
            dept.setCityId(cityId);
            dept.setCity(dictDefService.qryItemName("CITY",cityId));
        }
        sysDeptService.save(dept);
        //刷新redis里的记录
        flushRedis(dept.getCityId());
        return R.ok();
    }

    private void flushRedis(String cityId){
        Map<String, Object> map = new HashMap<>();
        map.put("cityFilter", cityId);
        List<SysDeptEntity> deptList = sysDeptService.queryList(map);
        J2CacheUtils.set(Constant.CITY_DEPT+"_"+cityId, deptList, Constant.ALIVE_SECONDS, false);
    }

    /**
     * 修改部门
     *
     * @param dept 部门
     * @return R
     */
    @SysLog("修改部门")
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update(@RequestBody SysDeptEntity dept) {
        if(getUserId()==Constant.SUPER_ADMIN||"Z".equals(getUser().getCityId())){//管理员
            dept.setCity(dictDefService.qryItemName("CITY",dept.getCityId()));
        }else{
            String cityId = getUser().getCityId();
            dept.setCityId(cityId);
            dept.setCity(dictDefService.qryItemName("CITY",cityId));
        }
        sysDeptService.update(dept);
        flushRedis(dept.getCityId());
        return R.ok();
    }

    /**
     * 删除部门
     *
     * @param deptId 主键
     * @return R
     */
    @SysLog("删除部门")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        sysDeptService.delete(deptId);

        return R.ok();
    }

}
