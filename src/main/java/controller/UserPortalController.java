package controller;
import dataBean.SysUserObject;
import framework.CallResult;
import model.SysUserRoleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pgMapping.enumEntity.EnumSysUserRole;
import service.impl.SysUserServiceImpl;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UserPortalController {
    @Autowired
    SysUserServiceImpl sysUserService;

    @RequestMapping("user/updateSysUserRoleDetail.do")
    @ResponseBody
    public CallResult<Object> updateUser(@RequestBody SysUserRoleDetail json,HttpServletRequest request){
        CallResult<Object> res = new CallResult<>();
        res.setFlag(sysUserService.saveOrUpdate(json,SysUserRoleDetail.class));
        return res;
    }

    /**
     * 用户登录
     * @param loginName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("user/userLogin.do")
    @ResponseBody
    public CallResult<Object> userLogin(String loginName, String password, HttpServletRequest request){
        CallResult<Object> res = new CallResult<>();
        SysUserObject userObject = sysUserService.checkLoginData(loginName,password);
        if( userObject !=null){
            EnumSysUserRole role = userObject.extra.getRole();
            request.getSession().setAttribute("loginState",true);
            request.getSession().setAttribute("role",role);
            res.setFlag(true);
            res.setData(userObject);
        }
        else{
            res.setFlag(false);
        }
        return  res;
    }

    /**
     * 用户注册
     * @param loginName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("user/registerUser.do")
    @ResponseBody
    public CallResult<Object> registerUser(String loginName, String password, HttpServletRequest request){
        CallResult<Object> res = new CallResult<>();
        res.setFlag(sysUserService.registerUser(loginName,password));
        return res;
    }

    /**
     * 检查用户权限
     * @param request
     * @return
     */
    public boolean checkUserRole(HttpServletRequest request){
        return false;
    }


}
