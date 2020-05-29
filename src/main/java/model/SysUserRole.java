package model;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 用户角色表 sys_user_role
 * @author: ly
 * @create: 2020-05-29 11:03
 **/
@Entity
@Table(name = "sys_user_role")
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = pgMapping.StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = pgMapping.IntArrayType.class),
        @TypeDef(name = "enum-type",typeClass = pgMapping.EnumType.class)
})
public class SysUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer gid;

    @Column(name = "projectgid")
    Integer projectgid;

    @Column(name = "name")
    String name;

    @Type(type = "string-array")
    @Column(name = "data_id_arr",length = 20)
    String[] data_id_arr;

    @Type(type = "string-array")
    @Column(name = "menu_id_arr",length = 20)
    String[] menu_id_arr;

    @Type(type = "string-array")
    @Column(name = "auth_arr")
    String[] auth_arr;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getProjectgid() {
        return projectgid;
    }

    public void setProjectgid(Integer projectgid) {
        this.projectgid = projectgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
