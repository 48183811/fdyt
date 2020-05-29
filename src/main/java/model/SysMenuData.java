package model;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 系统菜单数据表 sys_menudata
 * @author: ly
 * @create: 2020-05-29 11:05
 **/
@Entity
@Table(name = "sys_menudata")
public class SysMenuData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer gid;

    @Column(name = "projectgid")
    Integer projectgid;

    @Column(name = "name")
    String name;
    @Column(name = "dataid")
    String dataid;
    @Column(name = "menuid")
    String menuid;

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

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
}
