package com.doug.kplate.controller.recharge;

import com.doug.kplate.common.util.Query;
import com.doug.kplate.controller.sys.AbstractController;
import com.doug.kplate.dto.recharge.RechargeDto;
import com.doug.kplate.entity.recharge.Recharge;
import com.doug.kplate.entity.sys.SysRoleEntity;
import com.doug.kplate.service.recharge.RechargeService;
import com.doug.kplate.utils.Constant;
import com.doug.kplate.utils.PageUtils;
import com.doug.kplate.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recharge/recharge")
public class RechargeController extends AbstractController {
    @Autowired
    private RechargeService rechargeService;
    /**
     * 查询充值列表
     */
    @RequestMapping("list")
    @RequiresPermissions("recharge:recharge:list")
    public R list(@RequestParam Map<String, Object> params){
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("createUserId", getUserId());
        }

        //查询列表数据
        Query query = new Query(params);
        List<RechargeDto> list = rechargeService.queryList(query);
        int total = rechargeService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 审核
     * @return
     */
    public R audit(String auditStatus){

        return R.ok();
    }

}
