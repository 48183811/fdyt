package dataBean;

import model.SysUser;
import model.SysUserRole;

/**
 * @program: spring
 * @description: 用户信息整合
 * @author: ly
 * @create: 2020-05-12 18:06
 **/
public class SysUserObject{
    public SysUser user;
    public SysUserRole role;

    public SysUserObject( SysUser user, SysUserRole role) {
        this.user = user;
        this.role = role;
    }
}
