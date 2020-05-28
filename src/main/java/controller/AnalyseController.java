package controller;


import dto.*;
import framework.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.IAnalyseService;
import util.CoordinatesUtils;
import util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;


/****
 * 宗地规划情况分析Controller
 */
@Controller
public class AnalyseController {
    @Autowired
    IAnalyseService analyseService;

    /**
     * 查询规划情况
     * @param layerType 要查询的图层类别:TG_TDGHDL--土规、TG_JSYDGZQ--建设用地管制区、TG_JBNT--基本农田、TDXZ_DLTB2009--2009年土地现状
     * @param queryMode 查询方式:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 查询参数：对应查询方式，分别为宗地号、多边形坐标串、乡镇或村行政区代码
     * @return
     */
    @RequestMapping(value = "api/analyse/getPlanningInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public CallResult getPlanningInfo(String layerType,String queryMode,String parameter){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(parameter) || StringUtil.isEmpty(layerType) ||  StringUtil.isEmpty(queryMode)){
            result.setFlag(false);
            result.setMsg("参数为空");
            return result;
        }
        String coordinates = null; //查询范围坐标串

        /********获取查询的几何范围坐标*******/
        if (queryMode.equals("landNumber")){
            //查询房地一体shp数据信息
            FDYT_DTO fdyt_dto = analyseService.get_FDYT_by_landNumber(parameter);
            if (fdyt_dto == null){
                result.setFlag(false);
                result.setMsg("户号错误，查询失败");
                return result;
            }
            coordinates = fdyt_dto.getCoordinates();
        }else if (queryMode.equals("polygon")){
            coordinates = parameter;
        }else if (queryMode.equals("town") || queryMode.equals("village") ){
            coordinates = getCantonCoordinates(queryMode,parameter);
            if (coordinates == null){
                result.setFlag(false);
                result.setMsg("行政区代码错误，查询失败");
                return result;
            }
        }else {
            result.setFlag(false);
            result.setMsg("查询类型错误，查询失败");
            return result;
        }


        /********获取分析数据*******/
        if (layerType.equals("TG_TDGHDL")){
            //查询土地规划地类情况
            List<AnalyseResultInfoDTO> list = analyseService.getTdghdlAnalyseInfo(queryMode,coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        if (layerType.equals("TG_JSYDGZQ")){
            //查询建设用地管制区情况
            List<AnalyseResultInfoDTO> list = analyseService.getJsydgzqAnalyseInfo(queryMode,coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        if (layerType.equals("TG_JBNT")){
            //查询基本农田情况
            List<AnalyseResultInfoDTO> list = analyseService.getJbntAnalyseInfo(queryMode,coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        if (layerType.equals("TDXZ_DLTB2009")){
            //查询2009年土地利用现状情况
            List<AnalyseResultInfoDTO> list = analyseService.getDltb2009AnalyseInfo(queryMode,coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        result.setFlag(false);
        result.setMsg("查询图层错误，查询失败");
        return result;
    }

    /**
     * 获取范围内的宗地面积情况分析
     * @param type 类型:buildingStructure--按建筑结果分类、isVillagers--按是否为本村村民分类
     * @param queryMode 查询方式:polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 对应查询方式，分别为多边形坐标串、乡镇或村行政区代码
     * @return
     */
    @RequestMapping(value = "api/analyse/getLandRightsInfoAnalyse.do",method = RequestMethod.POST)
    @ResponseBody
    public CallResult getLandRightsInfoAnalyse(String type,String queryMode,String parameter){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(queryMode) || StringUtil.isEmpty(parameter)){
            result.setFlag(false);
            result.setMsg("参数为空");
            return result;
        }
        //获取查询范围
        String coordinates = null;
        if (queryMode.equals("polygon")){
            coordinates = parameter;
        }else if (queryMode.equals("town") || queryMode.equals("village") ){
            coordinates = getCantonCoordinates(queryMode,parameter);
            if (coordinates == null){
                result.setFlag(false);
                result.setMsg("行政区代码错误，查询失败");
                return result;
            }
        }else {
            result.setFlag(false);
            result.setMsg("查询类型错误，查询失败");
            return result;
        }

        /********获取分析数据*******/
        if (type.equals("buildingStructure")){
            List<LandAreaAnalyseDTO> list = analyseService.getLandAreaAnalyseGroupByBuildingStructure(coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        if (type.equals("isVillagers")){
            List<LandAreaAnalyseDTO> list = analyseService.getLandAreaAnalyseGroupByIsVillagers(coordinates);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        result.setFlag(false);
        result.setMsg("查询类型错误，查询失败");
        return result;
    }

    /**
     * 下载规划分析情况
     * @param layerType 图层类型:TG_TDGHDL--土规、TG_JSYDGZQ--建设用地管制区、TG_JBNT--基本农田、TDXZ_DLTB2009--2009年土地现状
     * @param queryMode 查询方法:landNumber--按宗地号查询、polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 参数：对应查询方式，分别为宗地号、多边形坐标串、乡镇或村行政区代码
     */
    @RequestMapping(value = "api/analyse/downloadPlanningInfo.do",method = RequestMethod.POST)
    public void downloadPlanningInfo(String layerType, String queryMode, String parameter, HttpServletResponse response){
        if (StringUtil.isEmpty(parameter) || StringUtil.isEmpty(layerType) ||  StringUtil.isEmpty(queryMode)){
            return;
        }
        String coordinates = null; //查询范围坐标串

        /********获取查询的几何范围坐标*******/
        if (queryMode.equals("landNumber")){
            //查询房地一体shp数据信息
            FDYT_DTO fdyt_dto = analyseService.get_FDYT_by_landNumber(parameter);
            if (fdyt_dto == null){
                return;
            }
            coordinates = fdyt_dto.getCoordinates();
        }else if (queryMode.equals("polygon")){
            coordinates = parameter;
        }else if (queryMode.equals("town") || queryMode.equals("village") ){
            coordinates = getCantonCoordinates(queryMode,parameter);
            if (coordinates == null){
                return;
            }
        }else {
            return;
        }

        if (layerType.equals("TG_TDGHDL") || layerType.equals("TG_JSYDGZQ") || layerType.equals("TG_JBNT") || layerType.equals("TDXZ_DLTB2009")){
            //查询数据
            List<PlanningInfoDownload_DTO> list = analyseService.getPlanningInfoDownload(layerType,coordinates);
            try {
                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/msexcel; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String("房地一体合规分析统计一览表.xls".getBytes("UTF-8"), "ISO8859_1"));
                //下载
                analyseService.downloadPlanningInfo(layerType,list,response.getOutputStream());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载范围内的宗地面积信息情况
     * @param queryMode 查询方式:polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 参数：对应查询方式，分别为宗地号、多边形坐标串、乡镇或村行政区代码
     */
    @RequestMapping(value = "api/analyse/downloadLandRightsArea.do",method = RequestMethod.POST)
    public void downloadLandRightsArea(String queryMode, String parameter, HttpServletResponse response) {
        if (StringUtil.isEmpty(parameter) || StringUtil.isEmpty(queryMode)) {
            return;
        }
        String coordinates = null; //查询范围坐标串

        /********获取查询的几何范围坐标*******/
        if (queryMode.equals("polygon")) {
            coordinates = parameter;
        } else if (queryMode.equals("town") || queryMode.equals("village")) {
            coordinates = getCantonCoordinates(queryMode, parameter);
            if (coordinates == null) {
                return;
            }
        } else {
            return;
        }
        //查询数据
        List<LandRightsInfoAnalyseDTO> list = analyseService.getLandRightsAreaDownload(coordinates);
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/msexcel; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("房地一体面积统计一览表.xls".getBytes("UTF-8"), "ISO8859_1"));
            //下载
            analyseService.downloadLandRightsAreaInfo(list,response.getOutputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }








   /*******************************************************私有方法*********************************************************************/

    /**
     * 获取行政区范围坐标串
     * @param type 查询类型：town--乡镇、village--村
     * @param administrativeRegionCode 行政区代码
     * @return
     */
    private String getCantonCoordinates(String type,String administrativeRegionCode){
       String coordinates = null;
       if (type.equals("town")){
           XZQ_DTO xzq_dto = analyseService.getTown(administrativeRegionCode);
           if (xzq_dto == null){
               return null;
           }
           coordinates = xzq_dto.getCoordinates();
           if (CoordinatesUtils.isPolygon(coordinates)){
               coordinates = CoordinatesUtils.PolygonToMultiPolygon(coordinates);
           }
       }
       if (type.equals("village")){
           XZQ_DTO xzq_dto = analyseService.getVillage(administrativeRegionCode);
           if (xzq_dto == null){
               return null;
           }
           coordinates = xzq_dto.getCoordinates();
           if (CoordinatesUtils.isPolygon(coordinates)){
               coordinates = CoordinatesUtils.PolygonToMultiPolygon(coordinates);
           }
       }
       return coordinates;
   }

}
