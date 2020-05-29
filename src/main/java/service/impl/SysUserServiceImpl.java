package service.impl;

import dao.SysMenuDataDAO;
import dao.SysProjectDAO;
import dao.SysUserDAO;
import dao.SysUserRoleDAO;
import dataBean.SysUserObject;
import model.SysMenuData;
import model.SysProject;
import model.SysUser;
import model.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pgMapping.enumEntity.EnumSysUserRole;
import service.ISysUserService;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-12 14:45
 **/
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    SysUserDAO sysUserDAO;
    @Autowired
    SysMenuDataDAO sysMenuDataDAO;
    @Autowired
    SysProjectDAO sysProjectDAO;
    @Autowired
    SysUserRoleDAO sysUserRoleDAO;

    private Map<Type,Field> DaoMap ;


    public SysUserServiceImpl() {
    }

    @Override
    public SysUser get(Integer gid) {
        return sysUserDAO.get(gid);
    }

    @Override
    public boolean registerProject(String name, String info) {
        return sysProjectDAO.registerProject(name,info);
    }

    @Override
    public SysUserObject checkLoginData(String loginName, String password) {
        try{
            SysUser user = sysUserDAO.checkLoginData(loginName,password);
            if(user!=null){
                SysUserRole  role = sysUserRoleDAO.getRole(user.getRole(),user.getProjectgid());
                return new SysUserObject(user,role);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean registerUser(String loginName, String password){
        return sysUserDAO.registerUser(loginName,password);
    }


    @Override
    public boolean saveOrUpdate(Object model, Class cls) {
        if(DaoMap == null){
            initMapDAO();
        }
        Field f = DaoMap.get(cls);
        try{
            Method method = f.getType().getSuperclass().getSuperclass().getDeclaredMethod("sou",Object.class);
            method.invoke(f.get(this),model);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void initMapDAO(){
        DaoMap = new HashMap<>();
        Class c = this.getClass();
        // Field[] fs = c.getFields();
        Field[] fs = c.getDeclaredFields();
        for (Field field : fs) {
            //得到成员变量的类型的类类型
            Class fieldType = field.getType();
            Type type = ((ParameterizedTypeImpl)fieldType.getGenericSuperclass()).getActualTypeArguments()[0];
            //得到成员变量的名称
            DaoMap.put(type,field);
        }
    }

    @Override
    public SysUserRole getRoleByGid(Integer gid) {
        SysUserRole role = null;
        try {
            role = sysUserRoleDAO.get(gid);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }
}
