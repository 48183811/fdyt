package service;


import dto.LandBuildTimeCountDTO;
import dto.LandRightsInfoDTO;
import enm.RESULT;
import framework.PageBean;
import model.LandRightsInfo;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/***
 * 宗地权籍信息Service
 */
public interface ILandRightsInfoService {
    /**
     * 根据权利人姓名分页查询宗地权籍信息
     * @param obligee 权利人姓名/宗地号/身份证号
     * @param pb 分页
     * @return
     */
    RESULT queryByObligee(String obligee, PageBean pb);

    /**
     * 根据宗地号查询宗地权籍信息
     * @param landNumber 宗地号
     * @return 宗地附件分类列表
     */
    LandRightsInfo getByLandNumber(String landNumber);

    /**
     * 根据户号查询宗地权籍信息
     * @param householdNumber 户号
     * @return 宗地附件分类列表
     */
    LandRightsInfo getByHouseholdNumber(String householdNumber);

    /**
     * 保存宗地权籍信息
     * @param landRightsInfoDTO 宗地权籍信息DTO
     * @param userId 用户主键
     * @param presentTime 当前时间
     * @return
     */
    LandRightsInfo save(LandRightsInfoDTO landRightsInfoDTO, String userId, Date presentTime);

    /**
     * 更新宗地权籍信息
     * @param landRightsInfoDTO 宗地权籍信息DTO
     * @param userId 用户主键
     * @param presentTime 当前时间
     * @return
     */
    LandRightsInfo upd(LandRightsInfoDTO landRightsInfoDTO, String userId, Date presentTime);

    /**
     * 根据坐标范围查询面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param pb 分页
     * @return
     */
    RESULT queryByCoordinates(String type,String coordinates,Float maxLandArea,Float maxHousesArea,PageBean pb);


    /**
     * 根据坐标范围查询所有面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @return
     */
    List<LandRightsInfo> queryAllOverproof(String type, String coordinates, Float maxLandArea, Float maxHousesArea);

    /**
     * 下载超标查询结果详情
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param landRightsInfoList 查询结果列表
     * @param os 输出数据流
     * @return
     */
    Boolean downloadPlanningInfo(String type, List<LandRightsInfo> landRightsInfoList, OutputStream os);

    /**
     * 查询修建时间在某个时间节点前后的超标宗地数
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：MultiPolygon(((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904)))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param buildTime 修建时间
     * @return
     */
    List<LandBuildTimeCountDTO> queryLandTotalByBuildTime(String type, String coordinates, Float maxLandArea, Float maxHousesArea, Date buildTime);
}

