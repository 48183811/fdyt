package model;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 项目表  sys_project
 * @author: ly
 * @create: 2020-05-29 11:04
 **/
@Entity
@Table(name = "sys_project")
public class SysProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer gid;
    @Column(name = "name")
    String name;
    @Column(name = "info")
    String info;

    public SysProject(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
