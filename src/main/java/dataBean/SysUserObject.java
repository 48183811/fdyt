package dataBean;

import model.SysUser;
import model.SysUserExtra;
import model.SysUserRoleDetail;

/**
 * @program: spring
 * @description: 用户信息整合
 * @author: ly
 * @create: 2020-05-12 18:06
 **/
public class SysUserObject{
    public SysUserExtra extra;
    public SysUserRoleDetail detail;

    public SysUserObject( SysUserExtra extra, SysUserRoleDetail detail) {
        this.extra = extra;
        this.detail = detail;
    }



    public SysUserExtra getExtra() {
        return extra;
    }

    public void setExtra(SysUserExtra extra) {
        this.extra = extra;
    }

    public SysUserRoleDetail getDetail() {
        return detail;
    }

    public void setDetail(SysUserRoleDetail detail) {
        this.detail = detail;
    }
}
