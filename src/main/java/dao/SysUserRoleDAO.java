package dao;

import framework.DefBaseDAO;
import model.SysUser;
import model.SysUserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-29 11:27
 **/
@Service
public class SysUserRoleDAO extends DefBaseDAO<SysUserRole, Integer> {

    private final static String GET_ROLE = "FROM SysUserRole WHERE name = ? and  projectgid = ?";

    /**
     * 通过用户角色和项目gid获取对应的角色数据
     * @param role  SysUser 提供的role
     * @param projectgid  SysUser 提供的projectgid
     * @return
     */
    public SysUserRole getRole(String role, Integer projectgid){
        List<SysUserRole> list = find(GET_ROLE,role,projectgid);
        if(list.size() ==1){
            return list.get(0);
        }
        return null;
    }
}
