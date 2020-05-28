package service.impl;

import dao.SysUserDAO;
import dao.SysUserExtraDAO;
import dao.SysUserRoleDetailDAO;
import dataBean.SysUserObject;
import model.SysUser;
import model.SysUserExtra;
import model.SysUserRoleDetail;
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
    SysUserExtraDAO sysUserExtraDAO;
    @Autowired
    SysUserRoleDetailDAO sysUserRoleDetailDAO;

    private Map<Type,Field> DaoMap ;


    public SysUserServiceImpl() {
    }

    @Override
    public SysUser get(Integer gid) {
        return sysUserDAO.get(gid);
    }

    public SysUserRoleDetail get(EnumSysUserRole role){
        return sysUserRoleDetailDAO.get(role);
    }
    @Override
    public SysUserObject checkLoginData(String loginName, String password) {
        Integer gid = sysUserDAO.checkLoginData(loginName,password);
        try{
            if(gid!=null){
                SysUserExtra extra = sysUserExtraDAO.get(gid);
                SysUserRoleDetail detail = sysUserRoleDetailDAO.get(extra.getRole());
                return new SysUserObject(extra,detail);
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
    public String[] getMenuIDArr(EnumSysUserRole role) {
        SysUserRoleDetail detail = sysUserRoleDetailDAO.get(role);
        if(detail != null){
            return detail.getMenu_id_arr();
        }
        return null;
    }

    @Override
    public String[] getMenuIDArr(Integer gid) {
        SysUserExtra extra = sysUserExtraDAO.get(gid);
        if(extra !=null){
            SysUserRoleDetail detail = sysUserRoleDetailDAO.get(extra.getRole());
            if(detail != null){
                return detail.getMenu_id_arr();
            }
        }
        return null;
    }

    @Override
    public String[] getDataIDArr(EnumSysUserRole role) {
        SysUserRoleDetail detail = sysUserRoleDetailDAO.get(role);
        if(detail != null){
            return detail.getData_id_arr();
        }
        return null;
    }

    @Override
    public String[] getDataIDArr(Integer gid) {
        SysUserExtra extra = sysUserExtraDAO.get(gid);
        if(extra !=null){
            SysUserRoleDetail detail = sysUserRoleDetailDAO.get(extra.getRole());
            if(detail != null){
                return detail.getData_id_arr();
            }
        }
        return null;
    }

    @Override
    public boolean setDataIDArr(String s) {
        return false;
    }

    @Override
    public boolean setMenuIDArr(String s) {
        return false;
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


}
