package controller;


import com.google.gson.Gson;
import dto.LandBuildTimeCountDTO;
import dto.LandRightsInfoDTO;
import dto.XZQ_DTO;
import enm.RESULT;
import framework.CallResult;
import framework.PageBean;
import model.LandRightsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.IAnalyseService;
import service.ILandRightsInfoService;
import util.CoordinatesUtils;
import util.DateUtil;
import util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/****
 * 宗地权籍信息Controller
 */
@Controller
public class LandRightsInfoController {
    @Autowired
    ILandRightsInfoService landRightsInfoService;
    @Autowired
    IAnalyseService analyseService;

    /**
     * 根据权利人姓名分页查询宗地权籍信息
     * @param obligee 权利人姓名/户号/身份证号
     * @param pb 分页
     * @return
     */
    @RequestMapping("api/landRightsInfo/queryByObligee.do")
    @ResponseBody
    public CallResult queryByObligee(String obligee, PageBean pb){
        CallResult result = new CallResult();
        RESULT queryByCompany = landRightsInfoService.queryByObligee(obligee,pb);
        if (queryByCompany == RESULT.SUCCESS){
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(pb);
            return result;
        }else {
            result.setFlag(false);
            result.setMsg("查询失败");
            result.setData(null);
            return result;
        }
    }

    /**
     * 根据宗地号查询宗地权籍信息
     * @param landNumber 宗地号
     * @return 含宗地附件分类列表
     */
    @RequestMapping("api/landRightsInfo/getByLandNumber.do")
    @ResponseBody
    public CallResult getByLandNumber(String landNumber){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(landNumber)){
            result.setFlag(false);
            result.setMsg("宗地号为空");
            return result;
        }
        LandRightsInfo landRightsInfo = landRightsInfoService.getByLandNumber(landNumber);
        if (landRightsInfo != null){
            LandRightsInfoDTO landRightsInfoDTO = LandRightsInfoToLandRigthsInfoDTO(landRightsInfo);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(landRightsInfoDTO);
            return result;
        }else {
            result.setFlag(false);
            result.setMsg("查询失败");
            return result;
        }
    }

    /**
     * 根据户号查询宗地权籍信息
     * @param householdNumber 户号
     * @return 含宗地附件分类列表
     */
    @RequestMapping("api/landRightsInfo/getByHouseholdNumber.do")
    @ResponseBody
    public CallResult getByHouseholdNumber(String householdNumber){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(householdNumber)){
            result.setFlag(false);
            result.setMsg("户号为空");
            return result;
        }
        LandRightsInfo landRightsInfo = landRightsInfoService.getByHouseholdNumber(householdNumber);
        if (landRightsInfo != null){
            LandRightsInfoDTO landRightsInfoDTO = LandRightsInfoToLandRigthsInfoDTO(landRightsInfo);
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(landRightsInfoDTO);
            return result;
        }else {
            result.setFlag(false);
            result.setMsg("查询失败");
            return result;
        }
    }

    /**
     * 保存宗地权籍信息
     * @param landRightsInfoJson 宗地权籍信息Json
     * @param userId 用户主键
     * @return
     */
    @RequestMapping(value = "api/landRightsInfo/save.do",method = RequestMethod.POST)
    @ResponseBody
    public Object save(String landRightsInfoJson, String userId) {
        CallResult result = new CallResult();
        Gson gson = new Gson();
        LandRightsInfoDTO landRightsInfoDTO = gson.fromJson(landRightsInfoJson, LandRightsInfoDTO.class);
        Date presentTime = new Date();
        String check = checkForm("add", landRightsInfoDTO,userId);
        if (!check.equals("校验通过")) {
            result.setFlag(false);
            result.setMsg(check);
            return result;
        }
        LandRightsInfo save = landRightsInfoService.save(landRightsInfoDTO,userId,presentTime);
        if (save != null){
            result.setFlag(true);
            result.setMsg("保存成功");
            result.setData(save);
            return result;
        }
        result.setFlag(false);
        result.setMsg("保存失败");
        return result;
    }
    /**
     * 更新宗地权籍信息
     * @param landRightsInfoJson 宗地权籍信息Json
     * @param userId 用户主键
     * @return
     */
    @RequestMapping(value = "api/landRightsInfo/upd.do",method = RequestMethod.POST)
    @ResponseBody
    public Object upd(String landRightsInfoJson, String userId) {
        CallResult result = new CallResult();
        Gson gson = new Gson();
        LandRightsInfoDTO landRightsInfoDTO = gson.fromJson(landRightsInfoJson, LandRightsInfoDTO.class);
        Date presentTime = new Date();
        String check = checkForm("upd", landRightsInfoDTO,userId);
        if (!check.equals("校验通过")) {
            result.setFlag(false);
            result.setMsg(check);
            return result;
        }
        LandRightsInfo upd = landRightsInfoService.upd(landRightsInfoDTO,userId,presentTime);
        if (upd != null){
            result.setFlag(true);
            result.setMsg("更新成功");
            result.setData(upd);
            return result;
        }
        result.setFlag(false);
        result.setMsg("更新失败");
        return result;
    }


    /**
     * 查询面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param queryMode 查询方式:polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 对应查询方式，分别为多边形坐标串、乡镇或村行政区代码
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param pb 分页
     * @return
     */
    @RequestMapping(value = "api/landRightsInfo/queryForOverproof.do",method = RequestMethod.POST)
    @ResponseBody
    public CallResult queryForOverproof(String type,String queryMode,String parameter,String maxLandArea,String maxHousesArea,PageBean pb){
        CallResult result = new CallResult();
        //参数校验
        String check = queryByCoordinatesCheck(type, queryMode,parameter, maxLandArea, maxHousesArea);
        if (!check.equals("校验通过")){
            result.setFlag(false);
            result.setMsg(check);
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

        //面积转换（String => Float）
        Float landArea = new Float(0);
        Float housesArea = new Float(0);
        if (StringUtil.notEmpty(maxLandArea)){
            landArea = Float.valueOf(maxLandArea);
        }
        if (StringUtil.notEmpty(maxHousesArea)){
            housesArea = Float.valueOf(maxHousesArea);
        }
        //查询
        RESULT  query = landRightsInfoService.queryByCoordinates(type,coordinates,landArea,housesArea,pb);
        if (query == RESULT.SUCCESS){
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(pb);
            return result;
        }
        result.setFlag(false);
        result.setMsg("查询失败");
        result.setData(pb);
        return result;
    }

    /**
     * 下载面积超标准的宗地权籍信息
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param queryMode 查询方式:polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 对应查询方式，分别为多边形坐标串、乡镇或村行政区代码
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @return
     */
    @RequestMapping(value = "api/landRightsInfo/downloadOverproof.do",method = RequestMethod.POST)
    public void downloadOverproof(String type,String queryMode,String parameter,String maxLandArea,String maxHousesArea, HttpServletResponse response){
        String check = queryByCoordinatesCheck(type, queryMode,parameter, maxLandArea, maxHousesArea);
        if (!check.equals("校验通过")){
            return;
        }
        //获取查询范围
        String coordinates = null;
        if (queryMode.equals("polygon")){
            coordinates = parameter;
        }else if (queryMode.equals("town") || queryMode.equals("village") ){
            coordinates = getCantonCoordinates(queryMode,parameter);
            if (coordinates == null){
                return;
            }
        }else {
            return;
        }

        //面积转换（String => Float）
        Float landArea = new Float(0);
        Float housesArea = new Float(0);
        if (StringUtil.notEmpty(maxLandArea)){
            landArea = Float.valueOf(maxLandArea);
        }
        if (StringUtil.notEmpty(maxHousesArea)){
            housesArea = Float.valueOf(maxHousesArea);
        }

        //查询数据
        List<LandRightsInfo> list = landRightsInfoService.queryAllOverproof(type,coordinates,landArea,housesArea);
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/msexcel; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("房地一体超标查询统计一览表.xls".getBytes("UTF-8"), "ISO8859_1"));
            //下载
            landRightsInfoService.downloadPlanningInfo(type,list,response.getOutputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询修建时间在某年前后的超标宗地数
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param queryMode 查询方式:polygon--按多边形查询、town--按乡镇查询、 village--按村查询
     * @param parameter 对应查询方式，分别为多边形坐标串、乡镇或村行政区代码
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @param year 修建年份：2016
     * @return 返回数据中，计算的修建截止时间点为****年12月31日23时59分59秒
     */
    @RequestMapping("api/landRightsInfo/queryLandTotalByBuildYear.do")
    @ResponseBody
    public CallResult queryLandTotalByBuildYear(String type,String queryMode,String parameter,String maxLandArea,String maxHousesArea,String buildYear){
        CallResult result = new CallResult();
        //参数校验
        String check = queryByCoordinatesCheck(type, queryMode,parameter, maxLandArea, maxHousesArea);
        if (!check.equals("校验通过")){
            result.setFlag(false);
            result.setMsg(check);
            return result;
        }
        if (StringUtil.isEmpty(buildYear) || buildYear.length() != 4 || !StringUtil.isNumber(buildYear)){
            result.setFlag(false);
            result.setMsg("修建年份错误");
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

        //面积转换（String => Float）
        Float landArea = new Float(0);
        Float housesArea = new Float(0);
        if (StringUtil.notEmpty(maxLandArea)){
            landArea = Float.valueOf(maxLandArea);
        }
        if (StringUtil.notEmpty(maxHousesArea)){
            housesArea = Float.valueOf(maxHousesArea);
        }
        Date buildTime = DateUtil.parse(buildYear + "-12-31 23:59:59");
        List<LandBuildTimeCountDTO> list = landRightsInfoService.queryLandTotalByBuildTime(type,coordinates,landArea,housesArea,buildTime);
        if (list != null){
            result.setFlag(true);
            result.setMsg("查询成功");
            result.setData(list);
            return result;
        }
        result.setFlag(false);
        result.setMsg("查询失败");
        return result;
    }











   /*******************************************************私有方法*********************************************************************/


    /**
     * LandRightsInfo转LandRigthsInfoDTO
     * @param landRightsInfo
     * @return
     */
    private LandRightsInfoDTO LandRightsInfoToLandRigthsInfoDTO(LandRightsInfo landRightsInfo){
       LandRightsInfoDTO landRightsInfoDTO = new LandRightsInfoDTO();
       landRightsInfoDTO.setId(landRightsInfo.getId());
       landRightsInfoDTO.setLandNumber(landRightsInfo.getLandNumber());
       landRightsInfoDTO.setHouseholdNumber(landRightsInfo.getHouseholdNumber());
       landRightsInfoDTO.setLandTrait(landRightsInfo.getLandTrait());
       landRightsInfoDTO.setObligee(landRightsInfo.getObligee());
       if (landRightsInfo.getPopulation() == 0){
           landRightsInfoDTO.setPopulation(null);
       }else {
           landRightsInfoDTO.setPopulation(String.valueOf(landRightsInfo.getPopulation()));
       }
       landRightsInfoDTO.setProjectName(landRightsInfo.getProjectName());
       landRightsInfoDTO.setIdCard(landRightsInfo.getIdCard());
       landRightsInfoDTO.setPhone(landRightsInfo.getPhone());
       landRightsInfoDTO.setHouseNumber(landRightsInfo.getHouseNumber());
       landRightsInfoDTO.setBuildTime(landRightsInfo.getBuildTime().getTime());
       landRightsInfoDTO.setBuildingStructure(landRightsInfo.getBuildingStructure());
       if (landRightsInfo.getFloorsTotalNumber() == 0){
           landRightsInfoDTO.setFloorsTotalNumber(null);
       }else {
           landRightsInfoDTO.setFloorsTotalNumber(String.valueOf(landRightsInfo.getFloorsTotalNumber()));
       }
       landRightsInfoDTO.setLicenseNumber(landRightsInfo.getLicenseNumber());
       landRightsInfoDTO.setLandApprovedArea(landRightsInfo.getLandApprovedArea());
       landRightsInfoDTO.setHousesApprovedArea(landRightsInfo.getHousesApprovedArea());
       landRightsInfoDTO.setLandArea(landRightsInfo.getLandArea());
       landRightsInfoDTO.setHousesArea(landRightsInfo.getHousesArea());
       landRightsInfoDTO.setHousesComplianceCertificate(landRightsInfo.getHousesComplianceCertificate());
       if (landRightsInfo.getVillagers() == true){
           landRightsInfoDTO.setIsVillagers("是");
       }else {
           landRightsInfoDTO.setIsVillagers("否");
       }
       landRightsInfoDTO.setObligeeSign(landRightsInfo.getObligeeSign());
       landRightsInfoDTO.setRemarks(landRightsInfo.getRemarks());
       landRightsInfoDTO.setTown(landRightsInfo.getTown());
       landRightsInfoDTO.setVillage(landRightsInfo.getVillage());
       landRightsInfoDTO.setVillageGroup(landRightsInfo.getVillageGroup());
       landRightsInfoDTO.setLandEnclosureList(landRightsInfo.getLandEnclosureList());
       landRightsInfoDTO.setLandEnclosureFiles(landRightsInfo.getLandEnclosureFiles());
       return landRightsInfoDTO;
   }


    /**
     * 宗地权籍信息校验（保存/更新）
     * @param type 类型：add/upd
     * @param landRightsInfoDTO 宗地权籍信息
     * @param useId 用户主键
     * @return
     */
    private String checkForm(String type, LandRightsInfoDTO landRightsInfoDTO, String useId){
        if (type.equals("upd")){
            if (StringUtil.isEmpty(landRightsInfoDTO.getId())){
                return "主键为空";
            }
        }
       if (StringUtil.isEmpty(landRightsInfoDTO.getLandNumber())){
           return "宗地号不能为空";
       }
       if (landRightsInfoDTO.getLandNumber().length() >50){
            return "宗地号长度不能超过50";
       }
        if (StringUtil.isEmpty(landRightsInfoDTO.getHouseholdNumber())){
            return "户号不能为空";
        }
        if (landRightsInfoDTO.getHouseholdNumber().length() >50){
            return "户号长度不能超过50";
        }
       if (StringUtil.isEmpty(landRightsInfoDTO.getLandTrait())){
           return "宗地特征不能为空";
       }
       if (landRightsInfoDTO.getLandTrait().length() > 50){
            return "宗地特征长度不能超过50";
       }
       if (StringUtil.isEmpty(landRightsInfoDTO.getObligee())){
           return "权利人不能为空";
       }
       if (landRightsInfoDTO.getObligee().length() > 100){
           return "权利人长度不能超过100";
       }
       if (!StringUtil.isInteger(landRightsInfoDTO.getPopulation())){
           return "家庭人口数不能为非数字";
       }
       if (Integer.parseInt(landRightsInfoDTO.getPopulation()) > 9999){
           return "家庭人口数不能大于9999";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getProjectName()) && landRightsInfoDTO.getProjectName().length() > 200){
           return "项目名称长度不能超过200";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getIdCard()) && landRightsInfoDTO.getIdCard().length() > 255){
           return "身份证总长度不能超过255";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getPhone()) && landRightsInfoDTO.getPhone().length() > 50){
           return "联系方式总长度不能超过255";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getHouseNumber()) && landRightsInfoDTO.getHouseNumber().length() > 100){
           return "门牌号长度不能超过255";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getBuildingStructure()) && landRightsInfoDTO.getBuildingStructure().length() > 10){
           return "房屋结构长度不能超过255";
       }
       if (!StringUtil.isInteger(landRightsInfoDTO.getFloorsTotalNumber())){
           return "房屋总层数不能为非数字";
       }
       if (Integer.parseInt(landRightsInfoDTO.getFloorsTotalNumber()) > 9999){
           return "房屋总层数不能大于9999";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getLandNumber()) && landRightsInfoDTO.getLandNumber().length() > 200){
           return "批准文号长度不能超过200";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getHousesComplianceCertificate()) && landRightsInfoDTO.getHousesComplianceCertificate().length() > 200){
           return "房屋合规证明长度不能超过200";
       }
       if (!landRightsInfoDTO.getIsVillagers().equals("true") && !landRightsInfoDTO.getIsVillagers().equals("false")){
           return "是否本村村民值错误";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getObligeeSign()) && landRightsInfoDTO.getObligeeSign().length() > 100){
           return "户主或法人签字长度不能超过100";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getRemarks()) && landRightsInfoDTO.getRemarks().length() > 255){
           return "备注长度不能超过255";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getTown()) && landRightsInfoDTO.getTown().length() > 50){
           return "乡镇社区名称长度不能超过50";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getVillage()) && landRightsInfoDTO.getVillage().length() > 50){
           return "村名长度不能超过50";
       }
       if (StringUtil.notEmpty(landRightsInfoDTO.getVillageGroup()) && landRightsInfoDTO.getVillageGroup().length() > 50){
           return "村民组名称长度不能超过50";
       }
       if (StringUtil.isEmpty(useId)){
           return "用户主键为空";
       }
       if (useId.length() > 36){
           return "用户主键长度错误";
       }
        return "校验通过";
   }

    /**
     * 校验根据坐标范围查询面积超标准的宗地权籍信息参数
     * @param type 超标类型：land--土地超标、houses--建筑超标、landAndHouses--土地和建筑都超标、landOrHouses--土地或建筑超标
     * @param coordinates 坐标串：Polygon((106.745919227648 26.8039691337117,106.745990574984 26.8039544252928,106.745965358039 26.803855927904))
     * @param maxLandArea 最大占地面积，平方米
     * @param maxHousesArea 最大建筑面积，平方米
     * @return
     */
   private String queryByCoordinatesCheck(String type,String queryMode,String parameter,String maxLandArea,String maxHousesArea){

        if (StringUtil.isEmpty(type)){
            return "查询类型为空";
        }
        if (StringUtil.isEmpty(parameter)){
            return "查询参数为空";
        }
        if (StringUtil.isEmpty(queryMode)){
            return "查询方式为空";
        }
        if (type.equals("land")){
            if (StringUtil.isEmpty(maxLandArea)){
                return "最大占地面积为空";
            }
            if (!StringUtil.isNumber(maxLandArea)){
                return "最大占地面积是非数字";
            }
        }else if (type.equals("houses")){
           if (StringUtil.isEmpty(maxHousesArea)){
               return "最大建筑面积为空";
           }
           if (!StringUtil.isNumber(maxHousesArea)){
               return "最大建筑面积是非数字";
           }
       }else if (type.equals("landAndHouses") || type.equals("landOrHouses")){
           if (StringUtil.isEmpty(maxHousesArea)){
               return "最大建筑面积为空";
           }
           if (!StringUtil.isNumber(maxHousesArea)){
               return "最大建筑面积是非数字";
           }
           if (StringUtil.isEmpty(maxHousesArea)){
               return "最大建筑面积为空";
           }
           if (!StringUtil.isNumber(maxHousesArea)){
               return "最大建筑面积是非数字";
           }
       }else {
            return "查询类型错误";
        }
       return "校验通过";
   }

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
