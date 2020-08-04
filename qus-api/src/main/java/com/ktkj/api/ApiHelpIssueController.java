package com.ktkj.api;

import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.entity.HelpIssueVo;
import com.ktkj.entity.HelpTypeVo;
import com.ktkj.service.ApiHelpIssueService;
import com.ktkj.service.ApiHelpTypeService;
import com.ktkj.util.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-11-07 11:04:20
 */
@RestController
@RequestMapping("api/helpissue")
public class ApiHelpIssueController extends ApiBaseAction {
    @Autowired
    private ApiHelpIssueService helpIssueService;
    @Autowired
    private ApiHelpTypeService helpTypeService;

    /**
     * 查看帮助类型列表
     */
    @RequestMapping("/typeList")
    @IgnoreAuth
    public Object typeList() {

        List<HelpTypeVo> list = helpTypeService.queryList(new HashMap());

        return toResponsSuccess(list);
    }

    /**
     * 查看问题列表
     */
    @RequestMapping("/issueList")
    @IgnoreAuth
    public Object issueList(Long type_id) {

        Map params = new HashMap();
        params.put("type_id", type_id);
        List<HelpIssueVo> helpIssueList = helpIssueService.queryList(params);

        return toResponsSuccess(helpIssueList);
    }
}
