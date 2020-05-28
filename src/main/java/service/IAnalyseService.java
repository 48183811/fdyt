package service;


import dto.*;
import jxl.write.WriteException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/***
 * 宗地规划情况分析Service
 */
public interface IAnalyseService {

    /**
     * 根据宗地号获取房地一体shp信息及几何坐标串
     * @param landNumber 宗地号
     * @return 返回面积最大的几何图形
     */
    FDYT_DTO get_FDYT_by_landNumber(String landNumber);

    /**
     * 根据户号获取房地一体shp信息及几何坐标串
     * @param householdNumber 户号
     * @return
     */
    FDYT_DTO get_FDYT_by_householdNumber(String householdNumber);

    /**
     * 查询几何范围内的土地规划地类分析信息
     * @param queryMode 查询方式:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param coordinates 几何坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return
     */
    List<AnalyseResultInfoDTO> getTdghdlAnalyseInfo(String queryMode,String coordinates);

    /**
     * 查询几何范围内的建设用地管制区分析信息
     * @param queryMode 查询方式:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param coordinates 几何坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return
     */
    List<AnalyseResultInfoDTO> getJsydgzqAnalyseInfo(String queryMode,String coordinates);

    /**
     * 查询几何范围内的基本农田分析信息
     * @param queryMode 查询方式:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param coordinates 几何坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return
     */
    List<AnalyseResultInfoDTO> getJbntAnalyseInfo(String queryMode,String coordinates);

    /**
     * 查询几何范围内的2009年土地利用现状分析信息
     * @param queryMode 查询方式:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param coordinates 几何坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @return
     */
    List<AnalyseResultInfoDTO> getDltb2009AnalyseInfo(String queryMode,String coordinates);


    /**
     * 获取范围内的宗地面积统计信息（以建筑结构分组）
     * @param coordinates 坐标串
     * @return (同一宗地的多户，进行合并处理)
     */
    List<LandAreaAnalyseDTO> getLandAreaAnalyseGroupByBuildingStructure(String coordinates);

    /**
     * 获取范围内的宗地面积统计信息（以是否本村村民分组）
     * @param coordinates 坐标串
     * @return(同一宗地的多户，进行合并处理)
     */
    List<LandAreaAnalyseDTO> getLandAreaAnalyseGroupByIsVillagers(String coordinates);

    /**
     * 根据行政区代码获取乡镇shp信息及范围坐标串
     * @param administrativeRegionCode 行政区代码
     * @return
     */
    XZQ_DTO getTown(String administrativeRegionCode);

    /**
     * 根据行政区代码获取行政村shp信息及范围坐标串
     * @param administrativeRegionCode 行政区代码
     * @return
     */
    XZQ_DTO getVillage(String administrativeRegionCode);

     /**
     * 查询几何范围内要下载的规划数据信息
     * @param layerType 查询图层:TG_TDGHDL--土规、TG_JSYDGZQ--建设用地管制区、TG_JBNT--基本农田、TDXZ_DLTB2009--2009年土地现状
     * @param coordinates 坐标串
     * @return
     */
    List<PlanningInfoDownload_DTO> getPlanningInfoDownload(String layerType,String coordinates);

    /**
     * 下载规划分析数据
     * @param layerType 查询图层:TG_TDGHDL--土规、TG_JSYDGZQ--建设用地管制区、TG_JBNT--基本农田、TDXZ_DLTB2009--2009年土地现状
     * @param planningInfoList 规划信息列表
     * @param os 数据流
     * @return
     */
    Boolean downloadPlanningInfo(String layerType, List<PlanningInfoDownload_DTO> planningInfoList, OutputStream os);

    /**
     * 查询几何范围内要下载的宗地权籍面积信息
     * @param coordinates 坐标串
     * @return
     */
    List<LandRightsInfoAnalyseDTO> getLandRightsAreaDownload(String coordinates);

    Boolean downloadLandRightsAreaInfo(List<LandRightsInfoAnalyseDTO> landRightsArealist,OutputStream os);
}

