package model;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import pgMapping.enumEntity.EnumSysUserRole;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 用户额外的信息表  sys_user_extra
 * @author: ly
 * @create: 2020-05-11 17:54
 **/
@Entity
@Table(name = "sys_user_extra")
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = pgMapping.StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = pgMapping.IntArrayType.class),
        @TypeDef(name = "enum-type",typeClass = pgMapping.EnumType.class)
})
public class SysUserExtra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer gid;

    @Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "enum-type")
    @Column(name = "role",columnDefinition = "sys_user_role")
    EnumSysUserRole role;


    @Column(name = "group",length = 50)
    String group;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public EnumSysUserRole getRole() {
        return role;
    }

    public void setRole(EnumSysUserRole role) {
        this.role = role;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
