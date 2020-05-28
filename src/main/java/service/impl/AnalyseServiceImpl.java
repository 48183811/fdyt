package service.impl;


import dao.AnalyseDAO;
import dto.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jxl.Cell;
import jxl.CellFeatures;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;
import jxl.format.CellFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.IAnalyseService;
import util.DataTransformUtil;
import util.ExcelUtils;
import util.StringUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Boolean;
import java.util.ArrayList;
import java.util.List;


@Service
public class AnalyseServiceImpl implements IAnalyseService {
    @Autowired
    AnalyseDAO analyseDAO;

    @Override
    public FDYT_DTO get_FDYT_by_landNumber(String landNumber) {
        if (StringUtil.isEmpty(landNumber)){
            return null;
        }
        return analyseDAO.get_FDYT_by_landNumber(landNumber);
    }

    @Override
    public FDYT_DTO get_FDYT_by_householdNumber(String householdNumber) {
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }
        return analyseDAO.get_FDYT_by_householdNumber(householdNumber);
    }

    @Override
    public List<AnalyseResultInfoDTO> getTdghdlAnalyseInfo(String queryMode,String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        //查询与几何相交的规划地类图斑
        List<TG_TDGHDL_DTO> list = new ArrayList<>();
        if (queryMode.equals("landNumber")){
            list = analyseDAO.findTdghdlByCoordinates(coordinates);
        }else {
            list = analyseDAO.findTdghdlByCoordinates2(coordinates);
        }
        if (list == null || list.size() == 0){
            return null;
        }
        List<AnalyseResultInfoDTO> resultList = new ArrayList<>();  //分析计算结果列表
        Double area = new Double(0);  //面积
        String name = ""; // 地类名称
        for (int i = 0;i < list.size();i++){
            TG_TDGHDL_DTO tg_tdghdl_dto = list.get(i);
            if (i == 0){
                //查询计算面积、获取地类名称
                area += tg_tdghdl_dto.getIntersectArea();
                name = StringUtil.replaceBlank(tg_tdghdl_dto.getGhdlmc());
                if (list.size() == 1){
                    //列表长度等于1，循环完毕，添加地类信息到resultList
                    if (area < 0.005){
                        //面积太小，四舍五入后不足0.01，
                        continue;
                    }
                    AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                    analyseResultInfoDTO.setName(name);
                    analyseResultInfoDTO.setArea(String.format("%.2f",area));
                    resultList.add(analyseResultInfoDTO);
                }
                continue;
            }
            if (!name.equals(StringUtil.replaceBlank(tg_tdghdl_dto.getGhdlmc()))){
                //地类名称改变，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    area = 0.0;  //重置面积
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(name);
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
                area = 0.0;  //重置面积
            }
            //查询计算面积、获取地类名称
            area += tg_tdghdl_dto.getIntersectArea();
            name = StringUtil.replaceBlank(tg_tdghdl_dto.getGhdlmc());

            if (i == (list.size() - 1)){
                //列表循环完毕，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(name);
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
            }
        }
        return resultList;
    }

    @Override
    public List<AnalyseResultInfoDTO> getJsydgzqAnalyseInfo(String queryMode,String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        //查询与几何相交的图斑
        List<TG_JSYDGZQ_DTO> list = new ArrayList<>();
        if (queryMode.equals("landNumber")){
            list = analyseDAO.findJsydgzqByCoordinates(coordinates);
        }else {
            list = analyseDAO.findJsydgzqByCoordinates2(coordinates);
        }

        if (list == null || list.size() == 0){
            return null;
        }
        List<AnalyseResultInfoDTO> resultList = new ArrayList<>();  //分析计算结果列表
        Double area = new Double(0);  //面积
        String name = ""; // 地类名称
        for (int i = 0;i < list.size();i++){
            TG_JSYDGZQ_DTO tg_jsydgzq_dto = list.get(i);
            if (i == 0){
                //查询计算面积、获取地类名称
                area += tg_jsydgzq_dto.getIntersectArea();
                name = StringUtil.replaceBlank(tg_jsydgzq_dto.getGzqlxdm());
                if (list.size() == 1){
                    //列表长度等于1，循环完毕，添加地类信息到resultList
                    if (area < 0.005){
                        //面积太小，四舍五入后不足0.01，
                        continue;
                    }
                    AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                    analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                    analyseResultInfoDTO.setArea(String.format("%.2f",area));
                    resultList.add(analyseResultInfoDTO);
                }
                continue;
            }
            if (!name.equals(StringUtil.replaceBlank(tg_jsydgzq_dto.getGzqlxdm()))){
                //地类名称改变，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    area = 0.0;  //重置面积
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
                area = 0.0;  //重置面积
            }
            //查询计算面积、获取地类名称
            area += tg_jsydgzq_dto.getIntersectArea();
            name = StringUtil.replaceBlank(tg_jsydgzq_dto.getGzqlxdm());

            if (i == (list.size() - 1)){
                //列表循环完毕，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
            }
        }
        return resultList;
    }

    @Override
    public List<AnalyseResultInfoDTO> getJbntAnalyseInfo(String queryMode,String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        //查询与几何相交的图斑
        List<TG_JBNT_DTO> list = new ArrayList<>();
        if (queryMode.equals("landNumber")){
            list = analyseDAO.findJbntByCoordinates(coordinates);
        }else {
            list = analyseDAO.findJbntByCoordinates2(coordinates);
        }

        if (list == null || list.size() == 0){
            return null;
        }
        List<AnalyseResultInfoDTO> resultList = new ArrayList<>();  //分析计算结果列表
        Double area = new Double(0);  //面积
        String name = "基本农田"; // 地类名称
        for (int i = 0;i < list.size();i++){
            TG_JBNT_DTO tg_jbnt_dto = list.get(i);
            //查询计算面积
            area += tg_jbnt_dto.getIntersectArea();
            if (i == (list.size() - 1)){
                //列表循环完毕，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(name);
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
            }
        }
        return resultList;
    }

    @Override
    public List<AnalyseResultInfoDTO> getDltb2009AnalyseInfo(String queryMode,String coordinates) {
        if (StringUtil.isEmpty(coordinates) || StringUtil.isEmpty(queryMode)){
            return null;
        }
        //查询与几何相交的图斑
        List<TDXZ_DLTB2009_DTO> list = new ArrayList<>();
        if (queryMode.equals("landNumber")){
            list = analyseDAO.findDltb2009ByCoordinates(coordinates);
        }else {
            list = analyseDAO.findDltb2009ByCoordinates2(coordinates);
        }

        if (list == null || list.size() == 0){
            return null;
        }
        List<AnalyseResultInfoDTO> resultList = new ArrayList<>();  //分析计算结果列表
        Double area = new Double(0);  //面积
        String name = ""; // 地类名称
        for (int i = 0;i < list.size();i++){
            TDXZ_DLTB2009_DTO tdxz_dltb2009_dto = list.get(i);
            if (i == 0){
                //查询计算面积、获取地类名称
                area += tdxz_dltb2009_dto.getIntersectArea();
                name = StringUtil.replaceBlank(tdxz_dltb2009_dto.getDlmc());
                if (list.size() == 1){
                    //列表长度等于1，循环完毕，添加地类信息到resultList
                    if (area < 0.005){
                        //面积太小，四舍五入后不足0.01，
                        continue;
                    }
                    AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                    analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                    analyseResultInfoDTO.setArea(String.format("%.2f",area));
                    resultList.add(analyseResultInfoDTO);
                }
                continue;
            }
            if (!name.equals(StringUtil.replaceBlank(tdxz_dltb2009_dto.getDlmc()))){
                //地类名称改变，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    area = 0.0;  //重置面积
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
                area = 0.0;  //重置面积
            }
            //查询计算面积、获取地类名称
            area += tdxz_dltb2009_dto.getIntersectArea();
            name = StringUtil.replaceBlank(tdxz_dltb2009_dto.getDlmc());

            if (i == (list.size() - 1)){
                //列表循环完毕，添加地类信息到resultList
                if (area < 0.005){
                    //面积太小，四舍五入后不足0.01，
                    continue;
                }
                AnalyseResultInfoDTO analyseResultInfoDTO = new AnalyseResultInfoDTO();
                analyseResultInfoDTO.setName(DataTransformUtil.jconstructionLandControlArea_transform(name));
                analyseResultInfoDTO.setArea(String.format("%.2f",area));
                resultList.add(analyseResultInfoDTO);
            }
        }
        return resultList;
    }

    @Override
    public List<LandAreaAnalyseDTO> getLandAreaAnalyseGroupByBuildingStructure(String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        List<LandRightsInfoAnalyseDTO> list = analyseDAO.findLandRightsInfoAnalyse(coordinates,"buildingStructure");
        if (list == null || list.size() == 0){
            return null;
        }
        List<LandAreaAnalyseDTO> resultList = new ArrayList<>();

        //分类统计面积信息
        int landCount = 0;  //宗地总数
        String landNumber = "";  //宗地号
        String name = "";       //类别名称
        Double landArea = 0.0;        //占地面积，平方米
        Double housesArea = 0.0;        //建筑面积，平方米
        List<String> landNumberList = new ArrayList<>();  //已统计的宗地号
        for (int i = 0;i < list.size();i++){
            LandRightsInfoAnalyseDTO landRightsInfoAnalyseDTO = list.get(i);
            if (i == 0){
                landNumber = StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getLandNumber());
                name = StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getBuildingStructure());
                landCount += 1;
                landArea += landRightsInfoAnalyseDTO.getLandArea();
                housesArea += landRightsInfoAnalyseDTO.getHousesArea();
                if (list.size() == 1){
                    //列表长度等于1，循环完毕，添加分析结果到resultList
                    LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
                    landAreaAnalyseDTO.setName(name);
                    landAreaAnalyseDTO.setLandCount(String.valueOf(landCount));
                    landAreaAnalyseDTO.setLandArea(String.format("%.2f",landArea));
                    landAreaAnalyseDTO.setHousesArea(String.format("%.2f",housesArea));
                    resultList.add(landAreaAnalyseDTO);
                }
                continue;
            }
            if (!name.equals(StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getBuildingStructure()))){
                //列表名称改变，添加分析结果到resultList
                LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
                landAreaAnalyseDTO.setName(name);
                landAreaAnalyseDTO.setLandCount(String.valueOf(landCount));
                landAreaAnalyseDTO.setLandArea(String.format("%.2f",landArea));
                landAreaAnalyseDTO.setHousesArea(String.format("%.2f",housesArea));
                resultList.add(landAreaAnalyseDTO);
                //重置计数
                name = StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getBuildingStructure());
                landCount = 0;
                landArea = 0.0;
                housesArea = 0.0;
            }
            if (!landNumberList.contains(StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getLandNumber()))){
                landNumber = StringUtil.replaceBlank(landRightsInfoAnalyseDTO.getLandNumber());
                landCount += 1;
                landArea += landRightsInfoAnalyseDTO.getLandArea();
                landNumberList.add(landNumber);
            }
            housesArea += landRightsInfoAnalyseDTO.getHousesArea();
            if (i == (list.size() - 1)) {
                //列表循环完毕，添加分析结果到resultList
                LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
                landAreaAnalyseDTO.setName(name);
                landAreaAnalyseDTO.setLandCount(String.valueOf(landCount));
                landAreaAnalyseDTO.setLandArea(String.format("%.2f",landArea));
                landAreaAnalyseDTO.setHousesArea(String.format("%.2f",housesArea));
                resultList.add(landAreaAnalyseDTO);
            }

        }
        if (resultList.size() > 0){
            //将建筑类型为"其他"的统计信息移到resultList末尾
            LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
            int index = -2;
            for (int i = 0;i < resultList.size();i++){
                LandAreaAnalyseDTO analyseDTO = resultList.get(i);
                if (analyseDTO.getName().equals("其它")){
                    landAreaAnalyseDTO.setName(analyseDTO.getName());
                    landAreaAnalyseDTO.setLandCount(analyseDTO.getLandCount());
                    landAreaAnalyseDTO.setLandArea(analyseDTO.getLandArea());
                    landAreaAnalyseDTO.setHousesArea(analyseDTO.getHousesArea());
                    index = i;
                    break;
                }
            }
            if (index != -2){
                resultList.remove(index);
                resultList.add(landAreaAnalyseDTO);
            }
        }
        return resultList;
    }

    @Override
    public List<LandAreaAnalyseDTO> getLandAreaAnalyseGroupByIsVillagers(String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return null;
        }
        List<LandRightsInfoAnalyseDTO> list = analyseDAO.findLandRightsInfoAnalyse(coordinates,"isVillagers");
        if (list == null || list.size() == 0){
            return null;
        }
        List<LandAreaAnalyseDTO> resultList = new ArrayList<>();

        //本村村民宗地分类统计信息
        int landCount_true = 0;  //宗地总数
        String landNumber = "";  //宗地号
        String name_true = "本村村民";       //类别名称
        Double landArea_true = 0.0;        //占地面积，平方米
        Double housesArea_true = 0.0;        //建筑面积，平方米
        //外来人员宗地分类统计信息
        int landCount_false = 0;  //宗地总数
        String name_false = "外来人员";       //类别名称
        Double landArea_false = 0.0;        //占地面积，平方米
        Double housesArea_false = 0.0;        //建筑面积，平方米
        List<String> landNumberList = new ArrayList<>();  //已统计的宗地号

        for (LandRightsInfoAnalyseDTO analyseDTO:list){
            landNumber = StringUtil.replaceBlank(analyseDTO.getLandNumber());
            if (analyseDTO.getVillagers()){
                //本村村民
                if (!landNumberList.contains(landNumber)){
                    //每宗地只统计一次占地面积
                    landCount_true += 1;
                    landArea_true += analyseDTO.getLandArea();
                    landNumberList.add(landNumber);
                }
                housesArea_true += analyseDTO.getHousesArea();
            }else {
                //外来人员
                if (!landNumberList.contains(landNumber)){
                    //每宗地只统计一次占地面积
                    landCount_false += 1;
                    landArea_false += analyseDTO.getLandArea();
                    landNumberList.add(landNumber);
                }
                housesArea_false += analyseDTO.getHousesArea();
            }
        }
        if (landCount_true > 0){
            //添加本村村民宗地分析信息到resultList
            LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
            landAreaAnalyseDTO.setName(name_true);
            landAreaAnalyseDTO.setLandCount(String.valueOf(landCount_true));
            landAreaAnalyseDTO.setLandArea(String.format("%.2f",landArea_true));
            landAreaAnalyseDTO.setHousesArea(String.format("%.2f",housesArea_true));
            resultList.add(landAreaAnalyseDTO);
        }
        if (landCount_false > 0){
            //添加外来人员宗地分析信息到resultList
            LandAreaAnalyseDTO landAreaAnalyseDTO = new LandAreaAnalyseDTO();
            landAreaAnalyseDTO.setName(name_false);
            landAreaAnalyseDTO.setLandCount(String.valueOf(landCount_false));
            landAreaAnalyseDTO.setLandArea(String.format("%.2f",landArea_false));
            landAreaAnalyseDTO.setHousesArea(String.format("%.2f",housesArea_false));
            resultList.add(landAreaAnalyseDTO);
        }
        return resultList;
    }

    @Override
    public XZQ_DTO getTown(String administrativeRegionCode) {
        if (StringUtil.isEmpty(administrativeRegionCode)){
            return null;
        }
        return analyseDAO.getTown(administrativeRegionCode);
    }

    @Override
    public XZQ_DTO getVillage(String administrativeRegionCode) {
        if (StringUtil.isEmpty(administrativeRegionCode)){
            return null;
        }
        return analyseDAO.getVillage(administrativeRegionCode);
    }

    @Override
    public List<PlanningInfoDownload_DTO> getPlanningInfoDownload(String layerType, String coordinates) {
//        TG_TDGHDL--土规、TG_JSYDGZQ--建设用地管制区、TG_JBNT--基本农田、TDXZ_DLTB2009--2009年土地现状
        if (StringUtil.isEmpty(layerType) || StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        if (layerType.equals("TG_TDGHDL")){
            return analyseDAO.findTdghdlDownloadByCoordinates2(coordinates);
        }
        if (layerType.equals("TG_JSYDGZQ")){
            return analyseDAO.findJsydgzqDownloadByCoordinates2(coordinates);
        }
        if (layerType.equals("TG_JBNT")){
            return analyseDAO.findJbntDownloadByCoordinates2(coordinates);
        }
        if (layerType.equals("TDXZ_DLTB2009")){
            return analyseDAO.findDltb2009DownloadByCoordinates2(coordinates);
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean downloadPlanningInfo(String layerType, List<PlanningInfoDownload_DTO> planningInfoList, OutputStream os){
        String sheetName = "";
        String titleName = "";
        String typeName = "";
        if (layerType.equals("TG_TDGHDL")){
            sheetName = "土地利用总体规划";
            titleName = "土地利用总体规划";
            typeName = "规划地类名称";
        }
        if (layerType.equals("TG_JSYDGZQ")){
            sheetName = "土地利用管制区";
            titleName = "土地利用管制区";
            typeName = "管制区类型名称";
            for (PlanningInfoDownload_DTO planningInfo:planningInfoList){
                planningInfo.setDlmc(DataTransformUtil.jconstructionLandControlArea_transform(StringUtil.replaceBlank(planningInfo.getDlmc())));
            }
        }
        if (layerType.equals("TG_JBNT")){
            sheetName = "基本农田";
            titleName = "基本农田";
            typeName = "地类名称";
            for (PlanningInfoDownload_DTO planningInfo:planningInfoList){
                planningInfo.setDlmc("基本农田");
            }
        }
        if (layerType.equals("TDXZ_DLTB2009")){
            sheetName = "2009年度土地利用现状";
            titleName = "2009年度土地利用现状";
            typeName = "地类名称";
        }

        //创建excel输出流
        try {
            /*****注：表格输出结果的地类面积为合并同一不动产单元号且同一地类名称数据****/
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            //创建工作表
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            Label label = null;
            sheet.mergeCells(0,0,4,0);
            //添加表格标题
            label = new Label(0,0,"房地一体合规分析（" + titleName + "）统计一览表",ExcelUtils.getCellFormat("tableTopTitle"));
            sheet.addCell(label);
            //添加表格行标题
            label = new Label(0,1,"宗地号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(1,1,"不动产单元号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(2,1,"权利人",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(3,1,typeName,ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(4,1,"面积（平方米）",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);

            //添加表格内容
            String landNumber = null;         //宗地号
            String householdNumber  = null;        //户号（不动产单元号）
            String obligee  = null;        //权利人或法人代表
            String householdNumberMerge  = null;        //合并户号（不动产单元号）
            String obligeeMerge  = null;        //合并权利人或法人代表
            String dlmc = null;        //规划地类名称
            Double intersectArea = 0.0; // 规划图斑与几何相交部分的面积（平方米）
            int reduceRowTotal = 0;      //总缩减行数,全局使用
            int mergeLandRowNum = 0;    //当前宗地的合并单元格行数。
            for (int i = 0;i < planningInfoList.size();i++) {
                PlanningInfoDownload_DTO planningInfo = planningInfoList.get(i);
                if (i==0){
                    landNumber = StringUtil.replaceBlank(planningInfo.getLandNumber());
                    householdNumber = StringUtil.replaceBlank(planningInfo.getHouseholdNumber());
                    obligee = StringUtil.replaceBlank(planningInfo.getObligee());
                    householdNumberMerge = StringUtil.replaceBlank(planningInfo.getHouseholdNumber());;
                    obligeeMerge = StringUtil.replaceBlank(planningInfo.getObligee());
                    dlmc = StringUtil.replaceBlank(planningInfo.getDlmc());
                    intersectArea = planningInfo.getIntersectArea();
                    if (planningInfoList.size() == 1){
                        //写出Excel
                        label = new Label(0,i+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(1,i+2,householdNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,i+2,obligee,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(3,i+2,dlmc,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(4,i+2,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                    }
                    continue;
                }

                if (landNumber.equals(StringUtil.replaceBlank(planningInfo.getLandNumber()))){
                    //宗地号同上一记录
                    if (i == (planningInfoList.size() - 1)){
                        /**列表循环完毕，写出Excel**/
                        if (!householdNumber.equals(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))) {
                            //单元号不同
                            householdNumberMerge = householdNumberMerge + "\n" + StringUtil.replaceBlank(planningInfo.getHouseholdNumber());
                            obligeeMerge = obligeeMerge + "\n" + StringUtil.replaceBlank(planningInfo.getObligee());
                            reduceRowTotal += 1;

                            int startRow = i-reduceRowTotal-mergeLandRowNum+2;
                            int endRow = i-reduceRowTotal+2;
                            label = new Label(0,startRow,landNumber,ExcelUtils.getCellFormat("tableBody"));
                            sheet.addCell(label);
                            label = new Label(1,startRow,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                            sheet.addCell(label);
                            label = new Label(2,startRow,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                            sheet.addCell(label);
                            label = new Label(3,endRow,dlmc,ExcelUtils.getCellFormat("tableBody"));
                            sheet.addCell(label);
                            label = new Label(4,endRow,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                            sheet.addCell(label);
                            if (mergeLandRowNum > 0){
                                //合并单元格
                                sheet.mergeCells(0,startRow,0,endRow);   //户号
                                sheet.mergeCells(1,startRow,1,endRow);   //不动产单元号
                                sheet.mergeCells(2,startRow,2,endRow);   //权利人
                            }
                        }else {
                            //单元号相同
                            if (householdNumberMerge.equals(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))){
                                /**该宗地的第一个单元号**/
                                if (dlmc.equals(StringUtil.replaceBlank(planningInfo.getDlmc()))){
                                    //地类未变化
                                    intersectArea += planningInfo.getIntersectArea();
                                    reduceRowTotal += 1;
                                    label = new Label(0,i-reduceRowTotal+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(3,i-reduceRowTotal+2,dlmc,ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(4,i-reduceRowTotal+2,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                }else {
                                    //地类变化
                                    //写出上一条记录
                                    label = new Label(0,i-reduceRowTotal+1,landNumber,ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(3,i-reduceRowTotal+1,dlmc,ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(4,i-reduceRowTotal+1,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    //写出当前记录
                                    label = new Label(0,i-reduceRowTotal+2,planningInfo.getLandNumber(),ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(3,i-reduceRowTotal+2,planningInfo.getDlmc(),ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    label = new Label(4,i-reduceRowTotal+2,String.format("%.2f",planningInfo.getIntersectArea()),ExcelUtils.getCellFormat("tableBody"));
                                    sheet.addCell(label);
                                    mergeLandRowNum += 1;
                                }
                                int startRow = i-reduceRowTotal-mergeLandRowNum+2;
                                int endRow = i-reduceRowTotal+2;
                                label = new Label(1,startRow,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                                sheet.addCell(label);
                                label = new Label(2,startRow,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                                sheet.addCell(label);
                                if (mergeLandRowNum > 0){
                                    //合并单元格
                                    sheet.mergeCells(0,startRow,0,endRow);   //户号
                                    sheet.mergeCells(1,startRow,1,endRow);   //不动产单元号
                                    sheet.mergeCells(2,startRow,2,endRow);   //权利人
                                }
                            }else {
                                /***该宗地非第一个单元号**/
                                reduceRowTotal += 1;
                                int startRow = i-reduceRowTotal-mergeLandRowNum+2;
                                int endRow = i-reduceRowTotal+2;
                                label = new Label(1,startRow,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                                sheet.addCell(label);
                                label = new Label(2,startRow,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                                sheet.addCell(label);
                                if (mergeLandRowNum > 0){
                                    //合并单元格
                                    sheet.mergeCells(0,startRow,0,endRow);   //宗地号
                                    sheet.mergeCells(1,startRow,1,endRow);   //不动产单元号
                                    sheet.mergeCells(2,startRow,2,endRow);   //权利人
                                }
                            }
                        }
                        continue;
                    }

                    if (!householdNumber.equals(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))){
                        //单元号不同，记录合并要素
                        if (householdNumberMerge.contains(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))){
                            //该记录已合并
                            reduceRowTotal += 1;
                            continue;
                        }
                        householdNumberMerge = householdNumberMerge + "\n" + StringUtil.replaceBlank(planningInfo.getHouseholdNumber());
                        obligeeMerge = obligeeMerge + "\n" + StringUtil.replaceBlank(planningInfo.getObligee());
                        reduceRowTotal += 1;
                        continue;
                    }else {
                        if (!householdNumberMerge.equals(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))){
                            //非该宗地的第一个单元号，跳过
                            reduceRowTotal += 1;
                            continue;
                        }
                    }
                }else {
                    //宗地号不同上一记录
                    if (i == (planningInfoList.size() - 1)){
                        /**列表循环完毕，写出Excel**/
                        //上一条记录写出Excel
                        reduceRowTotal += 1;
                        int startRow = i-reduceRowTotal-mergeLandRowNum+2;
                        int endRow = i-reduceRowTotal+2;
                        label = new Label(1,startRow,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,startRow,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(0,i-reduceRowTotal+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(3,i-reduceRowTotal+2,dlmc,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(4,i-reduceRowTotal+2,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        if (mergeLandRowNum > 0){
                            //合并单元格
                            sheet.mergeCells(0,startRow,0,endRow);   //宗地号
                            sheet.mergeCells(1,startRow,1,endRow);   //不动产单元号
                            sheet.mergeCells(2,startRow,2,endRow);   //权利人
                        }

                        //当前记录写出Excel
                        label = new Label(0,i-reduceRowTotal+3,planningInfo.getLandNumber(),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(1,i-reduceRowTotal+3,planningInfo.getHouseholdNumber(),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,i-reduceRowTotal+3,planningInfo.getObligee(),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(3,i-reduceRowTotal+3,planningInfo.getDlmc(),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(4,i-reduceRowTotal+3,String.format("%.2f",planningInfo.getIntersectArea()),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        continue;
                    }
                }

                if (!landNumber.equals(StringUtil.replaceBlank(planningInfo.getLandNumber())) || !dlmc.equals(StringUtil.replaceBlank(planningInfo.getDlmc()))){
                    //宗地号或地类名称对比上一条改变，将上一条记录写出Excel
                    label = new Label(0,i-reduceRowTotal+1,landNumber,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(3,i-reduceRowTotal+1,dlmc,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(4,i-reduceRowTotal+1,String.format("%.2f",intersectArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);

                    if (!householdNumber.equals(StringUtil.replaceBlank(planningInfo.getHouseholdNumber()))){
                        /**当前记录宗地号对比上一条改变**/

                        //将上一块宗地需合并单元号的值写入Excel
                        int startRow = i-reduceRowTotal-mergeLandRowNum+1;
                        int endRow = i-reduceRowTotal+1;
                        label = new Label(1,startRow,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,startRow,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);

                        if (mergeLandRowNum > 0){
                            //上一块宗地合并单元格
                            sheet.mergeCells(0,startRow,0,endRow);   //宗地号
                            sheet.mergeCells(1,startRow,1,endRow);   //不动产单元号
                            sheet.mergeCells(2,startRow,2,endRow);   //权利人
                        }

                        //重置当前宗地参数
                        householdNumberMerge = StringUtil.replaceBlank(planningInfo.getHouseholdNumber());;
                        obligeeMerge = StringUtil.replaceBlank(planningInfo.getObligee());
                        landNumber = StringUtil.replaceBlank(planningInfo.getLandNumber());
                        householdNumber = StringUtil.replaceBlank(planningInfo.getHouseholdNumber());
                        obligee = StringUtil.replaceBlank(planningInfo.getObligee());
                        dlmc = StringUtil.replaceBlank(planningInfo.getDlmc());
                        intersectArea = planningInfo.getIntersectArea();
                        mergeLandRowNum = 0;
                        continue;
                    }else {
                        //地类改变，重置当前记录参数
                        dlmc = StringUtil.replaceBlank(planningInfo.getDlmc());
                        intersectArea = planningInfo.getIntersectArea();
                        mergeLandRowNum += 1;
                        continue;
                    }

                }

                //同不动产单元号且同地类名称，面积累加
                intersectArea += planningInfo.getIntersectArea();
                reduceRowTotal += 1;
            }

            //设置列宽
            sheet.setColumnView(0,30);
            sheet.setColumnView(1,40);
            sheet.setColumnView(2,20);
            sheet.setColumnView(3,25);
            sheet.setColumnView(4,30);

            workbook.write();
            workbook.close();
            os.flush();
            os.close();
        }catch  (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<LandRightsInfoAnalyseDTO> getLandRightsAreaDownload(String coordinates) {
        if (StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return analyseDAO.findLandRightsAreaDownload(coordinates);
    }

    @Override
    public Boolean downloadLandRightsAreaInfo(List<LandRightsInfoAnalyseDTO> landRightsArealist, OutputStream os) {
        //创建excel输出流
        try {
            /*****注：表格输出结果的占地面积为宗地的占地面积，建筑面积为同一宗地不同单元号的建筑面积和****/
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            //创建工作表
            WritableSheet sheet = workbook.createSheet("sheet", 0);
            Label label = null;
            sheet.mergeCells(0,0,6,0);
            //添加表格标题
            label = new Label(0,0,"房地一体面积统计一览表",ExcelUtils.getCellFormat("tableTopTitle"));
            sheet.addCell(label);
            //添加表格行标题
            label = new Label(0,1,"宗地号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(1,1,"不动产单元号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(2,1,"权利人",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(3,1,"是否本村村民",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(4,1,"房屋结构",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(5,1,"占地面积（平方米）",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(6,1,"建筑面积（平方米）",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);

            String landNumber = null;         //宗地号
            String householdNumber  = null;        //户号（不动产单元号）
            String obligee  = null;        //权利人或法人代表
            String isVillagers = null;        //是否本村村民
            String buildingStructure = null;  //房屋结构
            Double landArea = 0.0;          //占地面积，平方米
            Double housesArea = 0.0;        //建筑面积，平方米

            String householdNumberMerge  = null;        //合并户号（不动产单元号）
            String obligeeMerge  = null;        //合并权利人或法人代表
            String isVillagersMerge = null;        //合并是否本村村民
            String buildingStructureMerge = null;  //合并房屋结构

            //添加表格内容
            int reduceRowTotal = 0;      //总缩减行数,全局使用

            for (int i = 0;i < landRightsArealist.size();i++){
                LandRightsInfoAnalyseDTO landRightsAreaDTO = landRightsArealist.get(i);
                if (i==0){
                    landNumber = landRightsAreaDTO.getLandNumber();
                    householdNumber = landRightsAreaDTO.getHouseholdNumber();
                    obligee = landRightsAreaDTO.getObligee();
                    if (landRightsAreaDTO.getVillagers()){
                        isVillagers = "是";
                        isVillagersMerge = "是";

                    }else {
                        isVillagers = "否";
                        isVillagersMerge = "否";
                    }
                    buildingStructure = landRightsAreaDTO.getBuildingStructure();
                    landArea = landRightsAreaDTO.getLandArea();
                    housesArea = landRightsAreaDTO.getHousesArea();

                    householdNumberMerge = landRightsAreaDTO.getHouseholdNumber();;
                    obligeeMerge = landRightsAreaDTO.getObligee();
                    buildingStructureMerge = landRightsAreaDTO.getBuildingStructure();
                    if (landRightsArealist.size() == 1){
                        //写出Excel
                        label = new Label(0,i+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(1,i+2,householdNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,i+2,obligee,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(3,i+2,isVillagers,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(4,i+2,buildingStructure,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(5,i+2,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(6,i+2,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                    }
                    continue;
                }
                if (landNumber.equals(landRightsAreaDTO.getLandNumber())){
                    //宗地号同上一记录,数据进行累加,不增行
                    householdNumberMerge = householdNumberMerge + "\n" + landRightsAreaDTO.getHouseholdNumber();;
                    obligeeMerge = obligeeMerge + "\n" + landRightsAreaDTO.getObligee();
                    if (landRightsAreaDTO.getVillagers()){
                        isVillagersMerge = isVillagersMerge + "\n是";

                    }else {
                        isVillagersMerge = isVillagersMerge + "\n否";
                    }
                    buildingStructureMerge = buildingStructureMerge + "\n" + landRightsAreaDTO.getBuildingStructure();
                    housesArea = housesArea + landRightsAreaDTO.getHousesArea();
                    reduceRowTotal += 1;
                }else {
                    //写出Excel
                    label = new Label(0,i-reduceRowTotal+1,landNumber,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(1,i-reduceRowTotal+1,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(2,i-reduceRowTotal+1,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(3,i-reduceRowTotal+1,isVillagersMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(4,i-reduceRowTotal+1,buildingStructureMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(5,i-reduceRowTotal+1,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(6,i-reduceRowTotal+1,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);

                    //重置参数
                    landNumber = landRightsAreaDTO.getLandNumber();
                    householdNumber = landRightsAreaDTO.getHouseholdNumber();
                    obligee = landRightsAreaDTO.getObligee();
                    if (landRightsAreaDTO.getVillagers()){
                        isVillagers = "是";
                        isVillagersMerge = "是";

                    }else {
                        isVillagers = "否";
                        isVillagersMerge = "否";
                    }
                    buildingStructure = landRightsAreaDTO.getBuildingStructure();
                    landArea = landRightsAreaDTO.getLandArea();
                    housesArea = landRightsAreaDTO.getHousesArea();

                    householdNumberMerge = landRightsAreaDTO.getHouseholdNumber();;
                    obligeeMerge = landRightsAreaDTO.getObligee();
                    buildingStructureMerge = landRightsAreaDTO.getBuildingStructure();
                }

                if (i == (landRightsArealist.size() - 1)){
                    //列表循环完毕,写出Excel
                    label = new Label(0,i-reduceRowTotal+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(1,i-reduceRowTotal+2,householdNumberMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(2,i-reduceRowTotal+2,obligeeMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(3,i-reduceRowTotal+2,isVillagersMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(4,i-reduceRowTotal+2,buildingStructureMerge,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(5,i-reduceRowTotal+2,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(6,i-reduceRowTotal+2,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                }
            }

            //设置列宽
            sheet.setColumnView(0,30);
            sheet.setColumnView(1,40);
            sheet.setColumnView(2,30);
            sheet.setColumnView(3,20);
            sheet.setColumnView(4,20);
            sheet.setColumnView(5,30);
            sheet.setColumnView(6,30);

            workbook.write();
            workbook.close();
            os.flush();
            os.close();
        }catch  (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
