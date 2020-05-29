package dao;

import framework.DefBaseDAO;
import model.SysProject;
import model.SysUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-29 11:25
 **/
@Service
public class SysProjectDAO extends DefBaseDAO<SysProject, Integer> {
    private final static String CHECK_PROJECT_NAME = "FROM SysProject WHERE name = ?";

    /**
     * 新建项目
     * @param name
     * @param info
     * @return
     */
    public boolean registerProject(String name , String info){
        List<SysProject> list = find(CHECK_PROJECT_NAME,name);
        if(list.size() == 1){
            return false;
        }
        else {
            SysProject project = new SysProject(name,info);
            add(project);
            return true;
        }
    }

}
