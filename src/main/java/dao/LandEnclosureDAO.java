package dao;

import framework.DefBaseDAO;
import model.LandEnclosure;
import org.springframework.stereotype.Service;
import util.StringUtil;

import java.util.List;

/***
 * 宗地附件信息DAO
 */
@Service
public class LandEnclosureDAO extends DefBaseDAO<LandEnclosure, String> {
    private final static String FIND_BY_LANDNUMBER = "FROM LandEnclosure WHERE landNumber = ? ";
    private final static String FIND_BY_HOUSEHOLDNUMBER = "FROM LandEnclosure WHERE householdNumber = ? ";


    /**
     * 根据宗地号查询所有宗地附件信息
     * @param landNumber 宗地号
     * @return
     */
    public List<LandEnclosure> findByLandNumber(String landNumber) {
        if (StringUtil.isEmpty(landNumber)){
            return null;
        }
        return find(FIND_BY_LANDNUMBER,null,null,landNumber);
    }

    /**
     * 根据户号查询所有宗地附件信息
     * @param householdNumber 户号
     * @return
     */
    public List<LandEnclosure> findByHouseholdNumber(String householdNumber) {
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }
        return find(FIND_BY_HOUSEHOLDNUMBER,null,null,householdNumber);
    }

}

