package controller;
import dataBean.SysUserObject;
import framework.CallResult;
import model.SysUser;
import model.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.impl.SysUserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@Controller
public class UserPortalController {
    @Autowired
    SysUserServiceImpl sysUserService;

    @RequestMapping("user/updateSysUserRoleDetail.do")
    @ResponseBody
    public CallResult<Object> updateUser(@RequestBody SysUser json, HttpServletRequest request){
        CallResult<Object> res = new CallResult<>();
        res.setFlag(sysUserService.saveOrUpdate(json,SysUser.class));
        return res;
    }

    /**
     * 用户登录 并在session中写入用户角色和项目
     * @param loginName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("user/userLogin.do")
    @ResponseBody
    public CallResult<Object> userLogin(String loginName, String password,String strCode, HttpServletRequest request){
        String code = request.getSession().getAttribute("strCode").toString();
        CallResult<Object> res = new CallResult<>(false);
        if(code.equals(strCode)){
            SysUserObject userObject = sysUserService.checkLoginData(loginName,password);
            if( userObject !=null){
                request.getSession().setAttribute("loginState",true);
                request.getSession().setAttribute("rolegid",userObject.role.getGid());
                res.setFlag(true);
                res.setMsg("window.location.href = 'main.html';");
            }
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
     * 新建项目
     * @param name
     * @param info
     * @param request
     * @return
     */
    @RequestMapping("project/registerProject.do")
    @ResponseBody
    public CallResult<Object> registerProject(String name, String info, HttpServletRequest request){
        CallResult<Object> res = new CallResult<>();
        res.setFlag(sysUserService.registerProject(name,info));
        return res;
    }

    /**
     * 检查用户权限
     * @param request
     * @return
     */
    @RequestMapping("user/checkUserRole.do")
    @ResponseBody
    public CallResult<Object> checkUserRole(HttpServletRequest request){
        CallResult<Object> res = new CallResult<>(false);
        Object obj = request.getSession().getAttribute("rolegid");
        if(obj != null){
            Integer rolegid = Integer.parseInt( obj.toString());
            res.setFlag(true);
            res.setData(sysUserService.getRoleByGid(rolegid));
        }
        else{
            res.setData("没有查询到相关信息");
            res.setMsg("window.location.href = 'login.html';");
        }
        return res;
    }

    /**
     * 用户注销登录
     * @param request
     */
    @RequestMapping("user/userLogout.do")
    @ResponseBody
    public CallResult<Object> userLogout(HttpServletRequest request){
        Enumeration<String> attrNames = request.getSession().getAttributeNames();
        while(attrNames.hasMoreElements()){
            request.getSession().removeAttribute(attrNames.nextElement());
        }
        return new CallResult<>(true);
    }


}
