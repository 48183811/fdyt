package model;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import pgMapping.*;
import pgMapping.enumEntity.EnumSysUserAuthorization;
import pgMapping.enumEntity.EnumSysUserRole;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 用户角色详细 ： 菜单数据 图层数据
 * @author: ly
 * @create: 2020-05-12 17:31
 **/
@Entity
@Table(name = "sys_user_role_detail")
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "enum-type",typeClass = pgMapping.EnumType.class),
        @TypeDef(name = "enum-type2",typeClass = pgMapping.EnumType.class),
        @TypeDef(name = "enum-array-type",typeClass = pgMapping.EnumArrayType.class)
})
public class SysUserRoleDetail {
    @Id
    @Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "enum-type")
    @Column(name = "role",columnDefinition = "sys_user_role")
    EnumSysUserRole role;

    @Type(type = "string-array")
    @Column(name = "data_id_arr",length = 20)
    String[] data_id_arr;

    @Type(type = "string-array")
    @Column(name = "menu_id_arr",length = 20)
    String[] menu_id_arr;

    @Type(type = "string-array")
    @Column(name = "auth_arr")
    String[] auth_arr;

    public EnumSysUserRole getRole() {
        return role;
    }

    public void setRole(EnumSysUserRole role) {
        this.role = role;
    }

    public String[] getData_id_arr() {
        return data_id_arr;
    }

    public void setData_id_arr(String[] data_id_arr) {
        this.data_id_arr = data_id_arr;
    }

    public String[] getMenu_id_arr() {
        return menu_id_arr;
    }

    public void setMenu_id_arr(String[] menu_id_arr) {
        this.menu_id_arr = menu_id_arr;
    }

    public String[] getAuth_arr() {
        return auth_arr;
    }

    public void setAuth_arr(String[] auth_arr) {
        this.auth_arr = auth_arr;
    }
}
