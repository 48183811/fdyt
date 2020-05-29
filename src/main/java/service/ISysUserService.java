package service;


import dataBean.SysUserObject;
import model.SysUser;
import model.SysUserRole;
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
     * 新建项目
     * @param name
     * @param info
     * @return
     */
    boolean registerProject(String name, String info);

    /**
     * 通过gid获取SysUserRole
     * @param gid
     * @return
     */
    SysUserRole getRoleByGid(Integer gid);
    /**
     * 保存或更新数据
     * @param object 数据实体
     * @param cls 对应的model.Class
     * @return
     */
    boolean saveOrUpdate(Object object,Class cls);
}
