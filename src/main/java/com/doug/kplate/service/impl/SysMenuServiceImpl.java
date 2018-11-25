package com.doug.kplate.service.impl;

import com.doug.kplate.dao.SysMenuDao;
import com.doug.kplate.entity.SysMenuEntity;
import com.doug.kplate.service.SysMenuService;
import com.doug.kplate.service.SysUserService;
import com.doug.kplate.utils.Constant;
import com.doug.kplate.utils.Constant.MenuType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId()) && menu.getStatus().equals("0")) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return sysMenuDao.queryListParentId(parentId);
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return sysMenuDao.queryNotButtonList();
    }

    @Override
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Override
    public SysMenuEntity queryObject(Long menuId) {
        return sysMenuDao.queryObject(menuId);
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return sysMenuDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysMenuDao.queryTotal(map);
    }

    @Override
    public void save(SysMenuEntity menu) {
        sysMenuDao.save(menu);
    }

    @Override
    public void update(SysMenuEntity menu) {
        sysMenuDao.update(menu);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] menuIds) {
        sysMenuDao.deleteBatch(menuIds);
    }

    @Override
    public List<SysMenuEntity> getMenuList(Map<String, Object> params) {
        return sysMenuDao.getMenuList(params);
    }

    @Override
    public void updateUserStatus(Map<String, Object> params) {
        sysMenuDao.updateUserStatus(params);
    }

    @Override
    public List<SysMenuEntity> queryUserList(Long userId) {
        return sysMenuDao.queryUserList(userId);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

        for (SysMenuEntity entity : menuList) {
            if (entity.getType() == MenuType.CATALOG.getValue()) {//目录
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }
}
