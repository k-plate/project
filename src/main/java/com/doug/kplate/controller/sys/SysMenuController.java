package com.doug.kplate.controller.sys;

import com.doug.kplate.common.exception.RRException;
import com.doug.kplate.common.util.Query;
import com.doug.kplate.dto.sys.SysMenuDto;
import com.doug.kplate.entity.sys.SysMenuEntity;
import com.doug.kplate.service.sys.ShiroService;
import com.doug.kplate.service.sys.SysMenuService;
import com.doug.kplate.utils.Constant;
import com.doug.kplate.utils.PageUtils;
import com.doug.kplate.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * 系统菜单
 *
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     */
    @RequestMapping("/nav")
    public R nav() {
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        return R.ok().put("menuList", menuList).put("permissions", permissions);
    }

    /**
     * 所有菜单列表-角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<SysMenuEntity> menuList = sysMenuService.queryList(query);
        if (menuList != null && menuList.size() > 0) {
            for (SysMenuEntity menu : menuList) {
                if (menu.getParentId() == 0 && menu.getType() == 1) {
                    //主菜单
                    menu.setType(0);
                } else if (menu.getParentId() != 0 && menu.getType() == 1) {
                    //子菜单
                    menu.setType(1);
                } else if (menu.getType() == 2) {
                    //按钮
                    menu.setType(2);
                }
            }
        }
        int total = sysMenuService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(menuList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 所有菜单列表-角色列表
     */
    @RequestMapping("/listTree")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenuEntity> list() {
        List<SysMenuEntity> menuList = sysMenuService.queryList(new HashMap<>());
        return menuList;
    }


    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public R select() {
        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return R.ok().put("menuList", menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public R info(@PathVariable("menuId") Long menuId) {
        SysMenuEntity menu = sysMenuService.queryObject(menuId);
        SysMenuDto dto = new SysMenuDto();
        entityToDto(menu, dto);
        return R.ok().put("menu", dto);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public R save(@RequestBody SysMenuDto menu) {
        //数据校验
        verifyForm(menu);
        menu.setCreatetime(new Timestamp(new Date().getTime()));
        SysMenuEntity entity = new SysMenuEntity();
        dtoToEntity(entity, menu);
        sysMenuService.save(entity);

        return R.ok();
    }

    /**
     * 修改/查看
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R update(@RequestBody SysMenuDto menu) {
        //数据校验
        verifyForm(menu);
        SysMenuEntity entity = new SysMenuEntity();
        dtoToEntity(entity, menu);
        sysMenuService.update(entity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    public R delete(long menuId) {
        if (menuId <= 31) {
            return R.error("系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return R.error("请先删除子菜单或按钮");
        }

        sysMenuService.deleteBatch(new Long[]{menuId});

        return R.ok();
    }

    /**
     * 查询主菜单列表
     */
    @RequestMapping("/getFirstMenuList")
    public R getFirstMenuList() {
        Map<String, Object> param = new HashMap<>();
        param.put("flag", 1);
        List<SysMenuEntity> firstMenuList = sysMenuService.getMenuList(param);
        return R.ok().put("firstMenuList", firstMenuList);
    }

    /**
     * 查询子菜单列表
     */
    @RequestMapping("/getSecondMenuList")
    public R getSecondMenuList() {
        Map<String, Object> param = new HashMap<>();
        param.put("flag", 2);
        List<SysMenuEntity> secondMenuList = sysMenuService.getMenuList(param);
        return R.ok().put("secondMenuList", secondMenuList);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuDto menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getType() != 0 && menu.getFirstMenu() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getType() != 0 && menu.getFirstMenu() != 0) {
            SysMenuEntity parentMenu = sysMenuService.queryObject(menu.getFirstMenu().longValue());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
//            if (parentType != Constant.MenuType.MENU.getValue()) {
//                throw new RRException("上级菜单只能为菜单类型");
//            }
            return;
        }
    }

    /**
     * 冻结用户、解冻用户
     */
    @RequestMapping("/updateStatus")
    public R delete(@RequestParam("menuId") Integer menuId, @RequestParam("status") String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("menuId", menuId);
        param.put("status", status);
        sysMenuService.updateUserStatus(param);
        return R.ok();
    }

    /**
     * 数据转换(entity---dto)
     */
    public void entityToDto(SysMenuEntity entity, SysMenuDto dto) {
        if (entity.getMenuId() != null) {
            dto.setMenuId(entity.getMenuId());
        }
        if (entity.getType() != null) {
            Integer type = entity.getType();
            if (type == 0) {
                //主菜单
            } else if (type == 1) {
                //子菜单
                dto.setFirstMenu(entity.getParentId().intValue());
            } else if (type == 2) {
                //按钮---需要获得该子菜单对应的主菜单
                SysMenuEntity subEntity = sysMenuService.queryObject(entity.getParentId());
                if (subEntity != null) {
                    dto.setFirstMenu(subEntity.getParentId().intValue());
                }
                dto.setSecondMenu(entity.getParentId().intValue());
            }
            dto.setType(type);
        }
        if (entity.getCreatetime() != null) {
            dto.setCreatetime(entity.getCreatetime());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            dto.setName(entity.getName());
        }
        if (StringUtils.isNotBlank(entity.getPerms())) {
            dto.setPerms(entity.getPerms());
        }
        if (StringUtils.isNotBlank(entity.getStatus())) {
            dto.setStatus(entity.getStatus());
        }
        if (entity.getOpen() != null) {
            dto.setOpen(entity.getOpen());
        }
        if (StringUtils.isNotBlank(entity.getUrl())) {
            dto.setUrl(entity.getUrl());
        }
    }

    /**
     * 数据转换(dto---entity)
     */
    public void dtoToEntity(SysMenuEntity entity, SysMenuDto dto) {
        if (dto.getMenuId() != null) {
            entity.setMenuId(dto.getMenuId());
        }
        if (dto.getType() != null) {
            Integer type = dto.getType();
            entity.setType(type);
            if (type == 0) {
                //主菜单
                entity.setParentId(new Long(0));
            } else if (type == 1) {
                //子菜单
                entity.setParentId(new Long(dto.getFirstMenu()));
            } else if (type == 2) {
                //按钮
                entity.setParentId(new Long(dto.getSecondMenu()));
            }
        }
        if (dto.getCreatetime() != null) {
            entity.setCreatetime(dto.getCreatetime());
        }
        if (StringUtils.isNotBlank(dto.getName())) {
            entity.setName(dto.getName());
        }
        if (StringUtils.isNotBlank(dto.getPerms())) {
            entity.setPerms(dto.getPerms());
        }
        if (StringUtils.isNotBlank(dto.getStatus())) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getOpen() != null) {
            entity.setOpen(dto.getOpen());
        }
        if (StringUtils.isNotBlank(dto.getUrl())) {
            entity.setUrl(dto.getUrl());
        }
    }
}
