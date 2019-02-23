package com.doug.kplate.controller.sys;

import com.doug.kplate.common.util.Query;
import com.doug.kplate.common.validator.Assert;
import com.doug.kplate.common.validator.ValidatorUtils;
import com.doug.kplate.common.validator.group.AddGroup;
import com.doug.kplate.common.validator.group.UpdateGroup;
import com.doug.kplate.entity.ExcelData;
import com.doug.kplate.entity.sys.SysRoleEntity;
import com.doug.kplate.entity.sys.SysUserEntity;
import com.doug.kplate.service.sys.SysUserRoleService;
import com.doug.kplate.service.sys.SysUserService;
import com.doug.kplate.utils.DateUtil;
import com.doug.kplate.utils.ExcelUtils;
import com.doug.kplate.utils.PageUtils;
import com.doug.kplate.utils.R;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * 系统用户
 *
 * @author honghu cloud
 * @technology +QQ： 2170983087
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        Map<String, Object> param = new HashMap<>();
        List<SysRoleEntity> sysRoleEntities = new ArrayList<>();
        List<SysUserEntity> userList = sysUserService.queryList(query);
        if (userList != null && userList.size() > 0) {
            for (int i = 0; i < userList.size(); i++) {
                //获得用户的角色
                param.put("userId", userList.get(i).getUserId());
                sysRoleEntities = sysUserRoleService.queryRoleByUserId(param);
                userList.get(i).setRoleName(sysRoleEntities.size() > 0 ? sysRoleEntities.get(0).getRoleName() : "");
            }
        }
        int total = sysUserService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //sha256加密
        password = new Sha256Hash(password, getUser().getSalt()).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (count == 0) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.queryObject(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        user.setRoleId(roleIdList != null && roleIdList.size() > 0 ? roleIdList.get(0) : new Long(0));
        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);
        user.setPassword(user.getCleanPassword());
        user.setCreateUserId(getUserId());
        user.setCreator(getUser().getUsername());
        user.setUpdateTime(new Timestamp(new Date().getTime()));
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setOperator(getUser().getUsername());
        sysUserService.save(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        user.setPassword(user.getCleanPassword());
        user.setCreateUserId(getUserId());
        user.setUpdateTime(new Timestamp(new Date().getTime()));
        user.setOperator(getUser().getUsername());
        sysUserService.update(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }
        sysUserService.deleteBatch(userIds);

        return R.ok();
    }

    /**
     * 冻结用户、解冻用户
     */
    @RequestMapping("/updateStatus")
    public R delete(@RequestParam("userId") Integer userId, @RequestParam("status") String status) {
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("status",status);
        sysUserService.updateUserStatus(param);
        return R.ok();
    }

    /**
     * 导出用户
     */

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void searchExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        ExcelData excelData = new ExcelData();
        List<Object> row = null;
        List<List<Object>> rows = new ArrayList<>();
        excelData.setName("用户管理-用户列表");
        Map<String, Object> param = new HashMap<>();
        List<SysRoleEntity> sysRoleEntities = new ArrayList<>();
        List<SysUserEntity> list = null;
        List<String> titles = new ArrayList<>();
        titles.add("账号");
        titles.add("姓名");
        titles.add("手机号");
        titles.add("所属角色");
        titles.add("创建时间");
        titles.add("状态");
        excelData.setTitles(titles);
        try {
            list = sysUserService.queryList(params);
            if (list != null && list.size() > 0) {
                for (SysUserEntity e:list) {
                    //获得用户的角色
                    param.put("userId",e.getUserId());
                    sysRoleEntities = sysUserRoleService.queryRoleByUserId(param);
                    row = new ArrayList<>();
                    row.add(e.getUsername());
                    row.add(e.getName());
                    row.add(e.getMobile());
                    row.add(sysRoleEntities.size() > 0 ? sysRoleEntities.get(0).getRoleName() : "");
                    row.add(DateUtil.formatDateToString(e.getCreateTime(), DateUtil.FORMAT_FULL));
                    if (e.getStatus() == 1) {
                        row.add("正常");
                    } else if (e.getStatus() == 0) {
                        row.add("禁用");
                    }
                    rows.add(row);
                }
            }
        } catch (Exception e) {
            logger.info("获得用户列表异常！" + e.getMessage());
        }
        excelData.setRows(rows);
        String fileName = "用户列表" + System.currentTimeMillis() + ".xls";
        try {
            ExcelUtils.exportExcel(response, fileName, excelData);
        } catch (Exception e) {
            logger.error("导出用户列表错误!",e.getMessage());
        }
    }

}
