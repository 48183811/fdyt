package model;

import javax.persistence.*;

/**
 * @program: spring
 * @description: 数据库表 sys_user
 * @author: ly
 * @create: 2020-05-11 17:46
 **/
@Entity
@Table(name = "sys_user")
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer gid;
    @Column(name = "login_name",length = 20)
    String login_name;
    @Column(name = "password",length = 32)
    String password;
    @Column(name = "token",length = 32)
    String token;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
