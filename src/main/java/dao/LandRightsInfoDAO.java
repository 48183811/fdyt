package dao;
import dto.LandBuildTimeCountDTO;
import dto.LandRightsInfoDTO;
import dto.LandRightsInfoQueryByObligeeDTO;
import enm.RESULT;
import framework.DefBaseDAO;
import framework.PageBean;
import model.LandRightsInfo;
import org.springframework.stereotype.Service;
import util.DateUtil;
import util.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 宗地权籍信息DAO
 */
@Service
public class LandRightsInfoDAO extends DefBaseDAO<LandRightsInfo, String> {
    private final static String QUERY_BY_OBLIGEE = "SELECT lri.id,lri.idcard,lri.landnumber,lri.householdnumber,lri.obligee,lri.town,lri.village,lri.villagegroup,lri.phone,ST_AsText(fhs.geom) as bufferCoordinates,fhs.jzgd as buildingHeight,fhs.dmgc as bottomElevation \n" +
            "FROM land_rights_info lri left join fdyt_hc_shp fhs on lri.householdNumber = fhs.bdcdyh WHERE 1=1 ";
    private final static String QUERY_BY_OBLIGEE_COUNT = "SELECT count(*) FROM land_rights_info lri left join fdyt_hc_shp fhs on lri.householdNumber = fhs.bdcdyh WHERE 1=1 ";
    private final static String QUERY = "SELECT * FROM land_rights_info WHERE 1=1 ";
    private final static String FIND_BY_COORDINATES = "SELECT lr.*,ST_AsText(fs.geom) as coordinates,ST_AsText(fhs.geom) as bufferCoordinates,fhs.jzgd as buildingHeight,fhs.dmgc as bottomElevation FROM \n" +
            "(fdyt_shp fs left join land_rights_info lr on fs.bdcdyh = lr.householdNumber) left join fdyt_hc_shp fhs on lr.householdNumber = fhs.bdcdyh WHERE ST_Intersects(fs.geom,ST_MultiPolygonFromText(?,4326)) ";
    private final static String FIND_BY_COORDINATES_COUNT = "SELECT count(*) FROM (fdyt_shp fs left join land_rights_info lr on fs.bdcdyh = lr.householdNumber) left join fdyt_hc_shp fhs on lr.householdNumber = fhs.bdcdyh WHERE \n" +
            "ST_Intersects(fs.geom,ST_MultiPolygonFromText(?,4326)) ";
    private final static String FIND_FOR_DOWNLOAD = "SELECT lr.* FROM fdyt_shp fs left join land_rights_info lr on fs.bdcdyh = lr.householdNumber WHERE ST_Intersects(fs.geom,ST_MultiPolygonFromText(?,4326)) ";
    private final static String FIND_FOR_COUNT = "SELECT count(*) FROM fdyt_shp fs left join land_rights_info lr on fs.bdcdyh = lr.householdNumber WHERE ST_Intersects(fs.geom,ST_MultiPolygonFromText(?,4326)) ";


    /**
     * 根据权利人姓名分页查询宗地权籍信息
     * @param obligee 权利人姓名/宗地号/身份证号
     * @param pb 分页
     * @return
     */
    public RESULT queryByObligee(String obligee, PageBean pb) {
        StringBuffer sql = new StringBuffer(QUERY_BY_OBLIGEE);
        StringBuffer sql_count = new StringBuffer(QUERY_BY_OBLIGEE_COUNT);
        List<Object> parm = new ArrayList<>();
        if (StringUtil.notEmpty(obligee)){
            sql.append(" and (lri.obligee like ? or lri.householdNumber like ? or lri.idCard like ?)");
            sql_count.append(" and (lri.obligee like ? or lri.householdNumber like ? or lri.idCard like ?)");
            parm.add("%" + obligee + "%");
            parm.add("%" + obligee + "%");
            parm.add("%" + obligee + "%");
        }
        return dtosToPageBean(pb, LandRightsInfoQueryByObligeeDTO.class,sql.toString(),parm,sql_count.toString(),parm);
    }

    /**
     * 根据宗地号查询宗地权籍信息
     * @param landNumber 宗地号
     * @return
     */
    public LandRightsInfo getByLandNumber(String landNumber){
        StringBuffer sql = new StringBuffer(QUERY);
        if (StringUtil.isEmpty(landNumber)){
            return null;
        }else {
            sql.append(" and landNumber = ? ");
        }
        List<LandRightsInfo> list = dtos(sql.toString(),LandRightsInfo.class,null,null,landNumber);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据户号查询宗地权籍信息
     * @param householdNumber 户号
     * @return
     */
    public LandRightsInfo getByHouseholdNumber(String householdNumber){
        StringBuffer sql = new StringBuffer(QUERY);
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }else {
            sql.append(" and householdNumber = ? ");
        }
        List<LandRightsInfo> list = dtos(sql.toString(),LandRightsInfo.class,null,null,householdNumber);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据坐标范围查询面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param pb 分页
     * @return
     */
    public RESULT queryByCoordinates(String type,String coordinates,Float maxLandArea,Float maxHousesArea,PageBean pb){
        StringBuffer sql = new StringBuffer(FIND_BY_COORDINATES);
        StringBuffer sql_count = new StringBuffer(FIND_BY_COORDINATES_COUNT);
        if (StringUtil.isEmpty(coordinates)){
            return RESULT.PARAMETER_EMPTY;
        }
        List<Object> parm = new ArrayList<>();
        if (type.equals("land")){
            sql.append(" and lr.equallandarea > ? ");
            sql_count.append(" and lr.equallandarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
        }else if (type.equals("houses")){
            sql.append(" and lr.housesarea > ? ");
            sql_count.append(" and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxHousesArea);
        }else if (type.equals("landAndHouses")){
            sql.append(" and lr.equallandarea > ?  and lr.housesarea > ? ");
            sql_count.append(" and lr.equallandarea > ?  and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else if (type.equals("landOrHouses")){
            sql.append("  and (lr.equallandarea > ?  or lr.housesarea > ?) ");
            sql_count.append("  and (lr.equallandarea > ?  or lr.housesarea > ?) ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else {
            return RESULT.PARAMETER_ERROR;
        }
        return dtosToPageBean(pb, LandRightsInfo.class,sql.toString(),parm,sql_count.toString(),parm);
    }

    /**
     * 根据坐标范围查询所有面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @return
     */
    public List<LandRightsInfo> queryAllOverproof(String type,String coordinates,Float maxLandArea,Float maxHousesArea){
        StringBuffer sql = new StringBuffer(FIND_FOR_DOWNLOAD);
        if (StringUtil.isEmpty(coordinates)){
            return  new ArrayList<>();
        }
        List<Object> parm = new ArrayList<>();
        if (type.equals("land")){
            sql.append(" and lr.equallandarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
        }else if (type.equals("houses")){
            sql.append(" and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxHousesArea);
        }else if (type.equals("landAndHouses")){
            sql.append(" and lr.equallandarea > ?  and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else if (type.equals("landOrHouses")){
            sql.append("  and (lr.equallandarea > ?  or lr.housesarea > ?) ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else {
            return new ArrayList<>();
        }
        sql.append(" ORDER BY lr.landnumber,lr.householdnumber");
        return dtos(sql.toString(),LandRightsInfo.class,null,null,parm.toArray());
    }


    /**
     * 查询修建时间在某个时间节点前后的超标宗地数
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param buildTime 修建时间
     * @return
     */
    public List<LandBuildTimeCountDTO> queryLandTotalByBuildTime(String type, String coordinates, Float maxLandArea, Float maxHousesArea, Date buildTime){
        StringBuffer sql_one = new StringBuffer(FIND_FOR_COUNT);
        StringBuffer sql_two = new StringBuffer(FIND_FOR_COUNT);
        if (StringUtil.isEmpty(coordinates)){
            return  new ArrayList<>();
        }
        List<Object> parm = new ArrayList<>();
        if (type.equals("land")){
            sql_one.append(" and lr.equallandarea > ? ");
            sql_two.append(" and lr.equallandarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
        }else if (type.equals("houses")){
            sql_one.append(" and lr.housesarea > ? ");
            sql_two.append(" and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxHousesArea);
        }else if (type.equals("landAndHouses")){
            sql_one.append(" and lr.equallandarea > ?  and lr.housesarea > ? ");
            sql_two.append(" and lr.equallandarea > ?  and lr.housesarea > ? ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else if (type.equals("landOrHouses")){
            sql_one.append("  and (lr.equallandarea > ?  or lr.housesarea > ?) ");
            sql_two.append("  and (lr.equallandarea > ?  or lr.housesarea > ?) ");
            parm.add(coordinates);
            parm.add(maxLandArea);
            parm.add(maxHousesArea);
        }else {
            return new ArrayList<>();
        }
        sql_one.append(" and buildTime <= ?");
        sql_two.append(" and buildTime > ?");
        parm.add(buildTime);
        Object sole_one = sole(sql_one.toString(), parm.toArray());
        Object sole_two = sole(sql_two.toString(), parm.toArray());

        List<LandBuildTimeCountDTO> list = new ArrayList<>();
        LandBuildTimeCountDTO buildTimeCountDTO_one = new LandBuildTimeCountDTO();
        LandBuildTimeCountDTO buildTimeCountDTO_two = new LandBuildTimeCountDTO();
        if(sole_one != null){
            buildTimeCountDTO_one.setName(DateUtil.getChineseYear(buildTime) + "（含）以前");
            buildTimeCountDTO_one.setLandCount(Integer.parseInt(sole_one.toString()));
        } else {
            buildTimeCountDTO_one.setName(DateUtil.getChineseYear(buildTime) + "（含）以前");
            buildTimeCountDTO_one.setLandCount(0);
        }
        if(sole_two != null){
            buildTimeCountDTO_two.setName(DateUtil.getChineseYear(buildTime) + "以后");
            buildTimeCountDTO_two.setLandCount(Integer.parseInt(sole_two.toString()));
        } else {
            buildTimeCountDTO_two.setName(DateUtil.getChineseYear(buildTime) + "以后");
            buildTimeCountDTO_two.setLandCount(0);
        }
        list.add(buildTimeCountDTO_one);
        list.add(buildTimeCountDTO_two);
        return list;
    }

}

