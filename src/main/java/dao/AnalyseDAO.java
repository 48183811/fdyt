package dao;

import dto.*;
import enm.RESULT;
import framework.DefBaseDAO;
import framework.PageBean;
import model.LandRightsInfo;
import org.springframework.stereotype.Service;
import util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/***
 * 宗地规划情况分析DAO
 */
@Service
public class AnalyseDAO extends DefBaseDAO<LandRightsInfo, String> {
    private final static String FIND_TG_TDGHDL_BY_COORDINATES = "SELECT t.gid,t.ghdlmc,t.ghdlmj, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,ST_MultiPolygonFromText(?,4326)),4524)) as intersectArea FROM tg_tdghdl_shp t WHERE \n" +
            "ST_Intersects(t.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.ghdlmc";
    private final static String FIND_TG_TDGHDL_BY_COORDINATES2 = "SELECT t.gid,t.ghdlmc,t.ghdlmj, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea FROM tg_tdghdl_shp t left join \n" +
            "(SELECT * FROM ( SELECT ROW_NUMBER () OVER ( PARTITION BY t1.qsbh ORDER BY t1.area desc ) AS rownum, * FROM ( SELECT *, st_area ( st_transform ( geom, 4524 ) ) AS area FROM fdyt_shp ) t1  ) AS t1 WHERE t1.rownum =1) f\n" +
            "on ST_Intersects(t.geom,f.geom) WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.ghdlmc";
    private final static String FIND_TG_TDGHDL_DOWNLOAD_BY_COORDINATES2 = "SELECT t.gid,t.ghdlmc as dlmc,t.ghdlmj as dlmj, ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea,lri.landnumber,lri.householdnumber,lri.obligee\n" +
            "FROM (tg_tdghdl_shp t left join fdyt_shp f on ST_Intersects(t.geom,f.geom)) left join land_rights_info lri on f.bdcdyh = lri.householdnumber \n" +
            "WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY lri.landnumber, lri.landarea desc,t.ghdlmc";
    private final static String FIND_TG_JSYDGZQ_DOWNLOAD_BY_COORDINATES2 = "SELECT t.gid,t.gzqlxdm as dlmc,t.gzqmj as dlmj, ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea,lri.landnumber,lri.householdnumber,lri.obligee\n" +
            "FROM (tg_jsydgzq_shp t left join fdyt_shp f on ST_Intersects(t.geom,f.geom)) left join land_rights_info lri on f.bdcdyh = lri.householdnumber \n" +
            "WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY lri.landnumber, lri.landarea desc,t.gzqlxdm";
    private final static String FIND_TG_JBNT_DOWNLOAD_BY_COORDINATES2 = "SELECT t.gid,t.dlmc,t.tbdlmj as dlmj, ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea,lri.landnumber,lri.householdnumber,lri.obligee\n" +
            "FROM (tg_jbnt_shp t left join fdyt_shp f on ST_Intersects(t.geom,f.geom)) left join land_rights_info lri on f.bdcdyh = lri.householdnumber \n" +
            "WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY lri.landnumber, lri.landarea desc,t.dlmc";
    private final static String FIND_TDXZ_DLTB2009_DOWNLOAD_BY_COORDINATES2 = "SELECT t.gid,t.dlmc,t.tbdlmj as dlmj, ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea,lri.landnumber,lri.householdnumber,lri.obligee\n" +
            "FROM (tdxz_dltb_2009_shp t left join fdyt_shp f on ST_Intersects(t.geom,f.geom)) left join land_rights_info lri on f.bdcdyh = lri.householdnumber \n" +
            "WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY lri.landnumber, lri.landarea desc,t.dlmc";

    private final static String FIND_TG_JSYDGZQ_BY_COORDINATES = "SELECT t.gid,t.gzqlxdm,t.gzqmj, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,ST_MultiPolygonFromText(?,4326)),4524)) as intersectArea FROM tg_jsydgzq_shp t WHERE \n" +
            "ST_Intersects(t.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.gzqlxdm";
    private final static String FIND_TG_JSYDGZQ_BY_COORDINATES2 = "SELECT t.gid,t.gzqlxdm,t.gzqmj, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea FROM tg_jsydgzq_shp t left join \n" +
            "(SELECT * FROM ( SELECT ROW_NUMBER () OVER ( PARTITION BY t1.qsbh ORDER BY t1.area desc ) AS rownum, * FROM ( SELECT *, st_area ( st_transform ( geom, 4524 ) ) AS area FROM fdyt_shp ) t1  ) AS t1 WHERE t1.rownum =1) f \n" +
            "on ST_Intersects(t.geom,f.geom) WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.gzqlxdm";
    private final static String FIND_TG_JBNT_BY_COORDINATES = "SELECT t.gid,t.dlmc, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,ST_MultiPolygonFromText(?,4326)),4524)) as intersectArea FROM tg_jbnt_shp t WHERE \n" +
            "ST_Intersects(t.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.dlmc";
    private final static String FIND_TG_JBNT_BY_COORDINATES2 ="SELECT t.gid,t.dlmc, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea FROM tg_jbnt_shp t left join \n" +
            "(SELECT * FROM ( SELECT ROW_NUMBER () OVER ( PARTITION BY t1.qsbh ORDER BY t1.area desc ) AS rownum, * FROM ( SELECT *, st_area ( st_transform ( geom, 4524 ) ) AS area FROM fdyt_shp ) t1  ) AS t1 WHERE t1.rownum =1) f \n" +
            "on ST_Intersects(t.geom,f.geom) WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.dlmc";
    private final static String FIND_TDXZ_DLTB_2009_BY_COORDINATES = "SELECT t.gid,t.dlbm,t.dlmc,t.qsxzdm, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,ST_MultiPolygonFromText(?,4326)),4524)) as intersectArea FROM tdxz_dltb_2009_shp t WHERE \n" +
            "ST_Intersects(t.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.dlmc";
    private final static String FIND_TDXZ_DLTB_2009_BY_COORDINATES2 = "SELECT t.gid,t.dlbm,t.dlmc,t.qsxzdm, ST_AsText(t.geom) as coordinates,ST_area( ST_Transform(st_intersection(t.geom,f.geom),4524)) as intersectArea ,ST_AsText(st_intersection(t.geom,f.geom)) as intersetCoordinates  FROM tdxz_dltb_2009_shp t left join \n" +
            "(SELECT * FROM ( SELECT ROW_NUMBER () OVER ( PARTITION BY t1.qsbh ORDER BY t1.area desc ) AS rownum, * FROM ( SELECT *, st_area ( st_transform ( geom, 4524 ) ) AS area FROM fdyt_shp ) t1  ) AS t1 WHERE t1.rownum =1) f \n" +
            "on ST_Intersects(t.geom,f.geom) WHERE ST_Intersects(f.geom,ST_MultiPolygonFromText(?,4326)) ORDER BY t.dlmc";
    private final static String GET_GEOMETRY_INTERSECTION_AREA = "SELECT st_area(ST_Transform (ST_Intersection(st_geometryfromtext(?,4326),st_geometryfromtext(?,4326)), 4524 )) as area";
    private final static String GET_FDYT = "SELECT temp.* FROM (SELECT *,ST_AsText(geom) as coordinates,st_area(ST_Transform(geom,4524)) as area FROM fdyt_shp) temp WHERE 1=1";
    private final static String GET_TOWN_BY_XZQDM = "SELECT s.gid,s.xzqdm,s.xzqmc,ST_AsText(s.geom) as coordinates FROM town_shp s WHERE s.xzqdm = ? ";
    private final static String GET_VILLAGE_BY_XZQDM = "SELECT s.gid,s.xzqdm,s.xzqmc,ST_AsText(s.geom) as coordinates FROM village_shp s WHERE s.xzqdm = ? ";
    private final static String FIND_LAND_RIGHTS_INFO_ANALYSE = "SELECT lrf.id,fs.gid,lrf.landnumber,lrf.landarea,lrf.housesarea,lrf.buildingstructure,lrf.isvillagers,lrf.town,lrf.village,lrf.villagegroup,ST_AsText ( fs.geom ) AS coordinates \n" +
            "FROM fdyt_shp fs left join land_rights_info lrf on fs.bdcdyh = lrf.householdNumber\n" +
            "WHERE	ST_Intersects ( fs.geom, ST_MultiPolygonFromText(?,4326)) ";
    private final static String FIND_LAND_RIGHTS_AREA_DOWNLOAD = "SELECT fs.gid,lrf.landnumber,lrf.householdnumber,lrf.obligee,lrf.landarea,lrf.housesarea,lrf.buildingstructure,lrf.isvillagers,lrf.town,lrf.village,lrf.villagegroup\n" +
            "FROM fdyt_shp fs left join land_rights_info lrf on fs.bdcdyh = lrf.householdNumber\n" +
            "WHERE	ST_Intersects ( fs.geom, ST_MultiPolygonFromText(?,4326)) ";
    /**
     * 根据宗地号获取房地一体shp信息及范围坐标串
     * @param landNumber 宗地号
     * @return 返回面积最大的几何图形
     */
    public FDYT_DTO get_FDYT_by_landNumber(String landNumber){
        StringBuffer sql = new StringBuffer(GET_FDYT);
        if (StringUtil.isEmpty(landNumber)){
            return null;
        }
        sql.append(" and temp.area = (SELECT max(a.area) FROM (SELECT st_area(ST_Transform(geom,4524)) as area FROM fdyt_shp WHERE qsbh = ?) a) ");
        List<FDYT_DTO> list = dtos(sql.toString(),FDYT_DTO.class,null,null,landNumber);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据户号获取房地一体shp信息及范围坐标串
     * @param householdNumber 户号
     * @return
     */
    public FDYT_DTO get_FDYT_by_householdNumber(String householdNumber){
        StringBuffer sql = new StringBuffer(GET_FDYT);
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }
        sql.append(" and bdcdyh = ?");
        List<FDYT_DTO> list = dtos(sql.toString(),FDYT_DTO.class,null,null,householdNumber);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取范围内的所有土地规划地类要素信息(直接查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以规划地类名称排序
     */
    public List<TG_TDGHDL_DTO> findTdghdlByCoordinates(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_TDGHDL_BY_COORDINATES);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(),TG_TDGHDL_DTO.class,null,null,coordinates,coordinates);
    }

    /**
     * 获取范围内的所有土地规划地类要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以规划地类名称排序(同一宗地号，只返回面积最大的)
     */
    public List<TG_TDGHDL_DTO> findTdghdlByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_TDGHDL_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(),TG_TDGHDL_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内要下载的土地规划地类要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串
     * @return 返回结果以户号、规划地类名称升序、占地面积降序排列
     */
    public List<PlanningInfoDownload_DTO> findTdghdlDownloadByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_TDGHDL_DOWNLOAD_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return dtos(sql.toString(),PlanningInfoDownload_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内要下载的建设用地管制区要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串
     * @return 返回结果以户号、管制区类型代码升序、占地面积降序排列
     */
    public List<PlanningInfoDownload_DTO> findJsydgzqDownloadByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JSYDGZQ_DOWNLOAD_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return dtos(sql.toString(),PlanningInfoDownload_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内要下载的基本农田要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串
     * @return 返回结果以户号、地类名称代码升序、占地面积降序排列
     */
    public List<PlanningInfoDownload_DTO> findJbntDownloadByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JBNT_DOWNLOAD_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return dtos(sql.toString(),PlanningInfoDownload_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内要下载的2009年现状要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串
     * @return 返回结果以户号、地类名称代码升序、占地面积降序排列
     */
    public List<PlanningInfoDownload_DTO> findDltb2009DownloadByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TDXZ_DLTB2009_DOWNLOAD_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return dtos(sql.toString(),PlanningInfoDownload_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内的所有建设用地管制区地类要素信息(直接查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以管制区类型代码排序
     */
    public List<TG_JSYDGZQ_DTO> findJsydgzqByCoordinates(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JSYDGZQ_BY_COORDINATES);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TG_JSYDGZQ_DTO.class,null,null,coordinates,coordinates);
    }

    /**
     * 获取范围内的所有建设用地管制区地类要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以管制区类型代码排序(同一宗地号，只返回面积最大的)
     */
    public List<TG_JSYDGZQ_DTO> findJsydgzqByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JSYDGZQ_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TG_JSYDGZQ_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内的所有基本农田要素信息(直接查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以地类名称排序
     */
    public List<TG_JBNT_DTO> findJbntByCoordinates(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JBNT_BY_COORDINATES);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TG_JBNT_DTO.class,null,null,coordinates,coordinates);
    }

    /**
     * 获取范围内的所有基本农田要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以地类名称排序(同一宗地号，只返回面积最大的)
     */
    public List<TG_JBNT_DTO> findJbntByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TG_JBNT_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TG_JBNT_DTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内的所有2009年土地利用现状要素信息(直接查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以地类名称排序
     */
    public List<TDXZ_DLTB2009_DTO> findDltb2009ByCoordinates(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TDXZ_DLTB_2009_BY_COORDINATES);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TDXZ_DLTB2009_DTO.class,null,null,coordinates,coordinates);
    }

    /**
     * 获取范围内的所有2009年土地利用现状要素信息(先查询出房地一体数据几何，再查询规划数据)
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return 返回结果以地类名称排序(同一宗地号，只返回面积最大的)
     */
    public List<TDXZ_DLTB2009_DTO> findDltb2009ByCoordinates2(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_TDXZ_DLTB_2009_BY_COORDINATES2);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        return dtos(sql.toString(), TDXZ_DLTB2009_DTO.class,null,null,coordinates);
    }

    /**
     * 获取两个几何图形的相交面积
     * @param coordinatesFirst  几何坐标串1
     * @param coordinatesSecond 几何坐标串2
     * @return
     */
    public Double getGeometryIntersectionArea(String coordinatesFirst,String coordinatesSecond){
        if (StringUtil.isEmpty(coordinatesFirst) | StringUtil.isEmpty(coordinatesSecond)){
            return 0.0;
        }
        return sole(GET_GEOMETRY_INTERSECTION_AREA, coordinatesFirst, coordinatesSecond);
    }

    /**
     * 获取范围内的宗地权籍分析信息列表
     * @param coordinates 坐标串
     * @param sort  排序字段
     * @return 返回结果以占地面积降序排列
     */
    public List<LandRightsInfoAnalyseDTO> findLandRightsInfoAnalyse(String coordinates,String sort){
        StringBuffer sql = new StringBuffer(FIND_LAND_RIGHTS_INFO_ANALYSE);
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        if (StringUtil.notEmpty(sort)){
            sql.append(" ORDER BY lrf." + sort + ",lrf.landarea desc");
        }
        return dtos(sql.toString(), LandRightsInfoAnalyseDTO.class,null,null,coordinates);
    }

    /**
     * 获取范围内的宗地面积分析信息下载列表
     * @param coordinates 坐标串
     * @return 返回结果以占地面积降序，宗地号升序排列
     */
    public List<LandRightsInfoAnalyseDTO> findLandRightsAreaDownload(String coordinates){
        StringBuffer sql = new StringBuffer(FIND_LAND_RIGHTS_AREA_DOWNLOAD);
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        sql.append(" ORDER BY lrf.householdnumber,lrf.landarea desc");
        return dtos(sql.toString(), LandRightsInfoAnalyseDTO.class,null,null,coordinates);
    }

    /**
     * 根据行政区代码获取乡镇shp信息及范围坐标串
     * @param administrativeRegionCode 行政区代码
     * @return
     */
    public XZQ_DTO getTown(String administrativeRegionCode){
        if (StringUtil.isEmpty(administrativeRegionCode)){
            return null;
        }
        List<XZQ_DTO> list = dtos(GET_TOWN_BY_XZQDM,XZQ_DTO.class,null,null,administrativeRegionCode);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据行政区代码获取行政村shp信息及范围坐标串
     * @param administrativeRegionCode 行政区代码
     * @return
     */
    public XZQ_DTO getVillage(String administrativeRegionCode){
        if (StringUtil.isEmpty(administrativeRegionCode)){
            return null;
        }
        List<XZQ_DTO> list = dtos(GET_VILLAGE_BY_XZQDM,XZQ_DTO.class,null,null,administrativeRegionCode);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}

