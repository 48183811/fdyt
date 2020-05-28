package dao;

import framework.DefBaseDAO;
import model.SysUserRoleDetail;
import org.springframework.stereotype.Service;
import pgMapping.enumEntity.EnumSysUserRole;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-12 17:39
 **/
@Service
public class SysUserRoleDetailDAO extends DefBaseDAO<SysUserRoleDetail, EnumSysUserRole> {
//    private final static String ENUMSelect = "FROM SysUserRoleDetail WHERE role = ?";
//
//    @Override
//    public SysUserRoleDetail get(sysUserRole s) {
//        List<SysUserRoleDetail> list = find(ENUMSelect,s);
//        if(list.size() ==1){
//            return list.get(0);
//        }else{
//            return null;
//        }
//    }

}
