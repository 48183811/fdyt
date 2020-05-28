package service;


import dataBean.SysUserObject;
import model.SysUser;
import pgMapping.enumEntity.EnumSysUserRole;

public interface ISysUserService {
    /**
     * 通过GID获取用户
     * @param gid
     * @return
     */
    SysUser get(Integer gid);

    /**
     * 用户登录验证
     * @param loginName
     * @param password
     * @return 成功的话返回用户的详细信息
     */
    SysUserObject checkLoginData(String loginName, String password);

    /**
     * 用户注册
     * @param loginName
     * @param password
     * @return
     */
    boolean registerUser(String loginName, String password);

    /**
     * 获取menuID数组
     * @param role 角色
     * @return 格式为{a,b,v,d}
     */
    String[] getMenuIDArr(EnumSysUserRole role);

    /**
     * 获取menuID数组
     * @param gid 用户gid
     * @return 格式为{a,b,v,d}
     */
    String[] getMenuIDArr(Integer gid);

    /**
     *
     * @param role 角色
     * @return 格式为{a,b,v,d}
     */
    String[] getDataIDArr(EnumSysUserRole role);

    /**
     *
     * @param gid 用户gid
     * @return 格式为{a,b,v,d}
     */
    String[] getDataIDArr(Integer gid);

    /**
     * 更新SysUserRoleDetail 的data_id_arr
     * @param s 格式为{a,b,v,d}
     * @return
     */
    boolean setDataIDArr(String s);

    /**
     * 更新SysUserRoleDetail 的menu_id_arr
     * @param s 格式为{a,b,v,d}
     * @return
     */
    boolean setMenuIDArr(String s);

    /**
     * 保存或更新数据
     * @param object 数据实体
     * @param cls 对应的model.Class
     * @return
     */
    boolean saveOrUpdate(Object object,Class cls);
}
