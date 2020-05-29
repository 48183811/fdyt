package dao;

import framework.DefBaseDAO;
import model.SysUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: spring
 * @description: 表sys_user 的DAO
 * @author: ly
 * @create: 2020-05-12 11:32
 **/
@Service
public class SysUserDAO extends DefBaseDAO<SysUser, Integer> {

    private final static String CHECK_LOGIN = "FROM SysUser WHERE login_name = ? and  password = ?";
    private final static String CHECK_LOGINNAME = "FROM SysUser WHERE login_name = ?";
    /**
     * 检测用户登录检测
     * @param loginName
     * @param password
     * @return 成功的话返回该用户的gid
     */
    public SysUser checkLoginData(String loginName, String password){
        List<SysUser> list = find(CHECK_LOGIN,loginName,password);
        if(list.size() ==1){
            return list.get(0);
        }
        return null;
    }

    /**
     * 用户注册
     * @param loginName
     * @param password
     * @return
     */
    public Boolean registerUser(String loginName, String password){
        List<SysUser> list = find(CHECK_LOGINNAME,loginName);
        if(list.size() == 1){
            return false;
        }
        else {
            SysUser user = new SysUser();
            user.setLogin_name(loginName);
            user.setPassword(password);
            add(user);
            return true;
        }
    }
}
