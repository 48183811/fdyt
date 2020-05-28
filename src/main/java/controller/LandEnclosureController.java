package controller;


import com.google.gson.Gson;
import dao.LandRightsInfoDAO;
import dto.LandEnclosureListDTO;
import enm.RESULT;
import framework.CallResult;
import framework.Executor;
import model.LandEnclosure;
import model.LandRightsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import service.ILandEnclosureService;
import service.ILandRightsInfoService;
import util.DataTransformUtil;
import util.FileUtils;
import util.StringUtil;

import java.util.Date;
import java.util.List;


/****
 * 宗地附件信息Controller
 */
@Controller
public class LandEnclosureController {
    @Autowired
    ILandEnclosureService landEnclosureService;
    @Autowired
    LandRightsInfoDAO landRightsInfoDAO;
    @Autowired
    ILandRightsInfoService landRightsInfoService;


    /**
     * 保存宗地附件文件和宗地附件信息
     * @param userId 用户主键
     * @param householdNumber 户号（不动产单元号）
     * @param files 文件列表
     * @param landEnclosureListJson 宗地附件信息列表Json
     * @return
     */
    @RequestMapping(value = "api/landEnclosure/save.do",method = RequestMethod.POST)
    @ResponseBody
    public Object save(String userId,String householdNumber, @RequestParam MultipartFile[] files,String landEnclosureListJson) {
        CallResult result = new CallResult();
        Gson gson = new Gson();
        LandEnclosureListDTO landEnclosureListDTO = gson.fromJson(landEnclosureListJson,LandEnclosureListDTO.class);
        List<LandEnclosure> landEnclosureList = landEnclosureListDTO.getLandEnclosureList();
        Date presentTime = new Date();
        String check = checkSaveForm(userId,files,landEnclosureList);
        if (!check.equals("校验通过")) {
            result.setFlag(false);
            result.setMsg(check);
            return result;
        }
        //获取宗地权籍信息
        LandRightsInfo landRightsInfo = landRightsInfoDAO.getByHouseholdNumber(householdNumber);
        if (landRightsInfo == null){
            result.setFlag(false);
            result.setMsg("户号错误");
            return result;
        }
        int saveSuccessNun = 0;
        for (int i = 0;i < files.length;i++){
            MultipartFile file = files[i];
            LandEnclosure landEnclosure = landEnclosureList.get(i);
            String fileType = DataTransformUtil.checkLandEnclosureFileType(landEnclosure.getFileType()) + "、" + landEnclosure.getFileType();
            //保存文件
            String webPath = landEnclosureService.saveFile(fileType,file,landRightsInfo);
            if (webPath == null){
                continue;
            }
            //保存宗地附件信息
            RESULT saveInfo = landEnclosureService.saveLandEnclosure(householdNumber,webPath,userId,presentTime,file,landEnclosure);
            if (saveInfo == RESULT.SUCCESS){
                saveSuccessNun += 1;
            }
        }
        //校验保存结果情况
        if (saveSuccessNun == files.length){
            result.setFlag(true);
            result.setMsg("保存成功");
            return result;
        }else if (saveSuccessNun > 0){
            result.setFlag(false);
            result.setMsg("部分保存成功");
            return result;
        }else {
            result.setFlag(false);
            result.setMsg("保存失败");
            return result;
        }
    }

    /**
     * 删除宗地附件文件和宗地附件信息
     * @param id 宗地附件主键
     * @return
     */
    @RequestMapping("api/landEnclosure/delete.do")
    @ResponseBody
    public CallResult delete(String id){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(id)){
            result.setFlag(false);
            result.setMsg("删除失败,附件主键为空");
            return result;
        }
        LandEnclosure landEnclosure = landEnclosureService.get(id);
        if (landEnclosure == null){
            result.setFlag(false);
            result.setMsg("删除失败,附件主键错误");
            return result;
        }
        //删除宗地附件信息
        RESULT deleteInfo = landEnclosureService.delLandEnclosure(id);
        if (deleteInfo == RESULT.SUCCESS){
            //执行异步任务，删除文件
            Executor executor = new Executor();
            try {
                executor.delFile(FileUtils.getSavePath(landEnclosure.getFilePath()));
            }catch(Exception e) {
                throw new RuntimeException("文件删除子任务出错");
            }
            result.setFlag(true);
            result.setMsg("删除成功");
            return result;
        }
        result.setFlag(false);
        result.setMsg("删除失败");
        return result;
    }

    /**
     * 更新宗地附件文件和宗地附件信息
     * @param userId 用户主键
     * @param id 附件主键
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "api/landEnclosure/upd.do",method = RequestMethod.POST)
    @ResponseBody
    public Object upd(String userId,String id, @RequestParam MultipartFile file) {
        CallResult result = new CallResult();
        Date presentTime = new Date();
        if (StringUtil.isEmpty(userId)){
            result.setFlag(false);
            result.setMsg("用户主键为空");
            return result;
        }
        if (StringUtil.isEmpty(id)){
            result.setFlag(false);
            result.setMsg("附件主键为空");
            return result;
        }
        if (file == null){
            result.setFlag(false);
            result.setMsg("附件为空");
            return result;
        }
        LandEnclosure landEnclosure = landEnclosureService.get(id);
        if (landEnclosure == null){
            result.setFlag(false);
            result.setMsg("附件主键错误");
            return result;
        }
        String landNumber = landEnclosure.getLandNumber();
        LandRightsInfo landRightsInfo = landRightsInfoService.getByLandNumber(landNumber);
        if (landRightsInfo == null){
            result.setFlag(false);
            result.setMsg("数据库中没有找到相应的宗地权籍信息");
            return result;
        }
        //保存文件
        String fileType = DataTransformUtil.checkLandEnclosureFileType(landEnclosure.getFileType()) + "、" + landEnclosure.getFileType();
        String webPath = landEnclosureService.saveFile(fileType,file,landRightsInfo);
        if (webPath == null){
            result.setFlag(false);
            result.setMsg("附件保存失败");
            return result;
        }
        //更新宗地权籍附件信息
        RESULT saveInfo = landEnclosureService.updLandEnclosure(userId,id,presentTime,webPath,file);
        if (saveInfo == RESULT.SUCCESS){
            //执行异步任务，删除文件
            Executor executor = new Executor();
            try {
                executor.delFile(FileUtils.getSavePath(landEnclosure.getFilePath()));
            }catch(Exception e) {
                throw new RuntimeException("文件删除子任务出错");
            }
            result.setFlag(true);
            result.setMsg("更新成功");
            return result;
        }
        result.setFlag(false);
        result.setMsg("更新失败");
        return result;
    }

    /**
     * 根据户号查询所有附件信息
     * @param householdNumber 户号
     * @return
     */
    @RequestMapping("api/landEnclosure/findHouseholdNumber.do")
    @ResponseBody
    public CallResult findHouseholdNumber(String householdNumber){
        CallResult result = new CallResult();
        if (StringUtil.isEmpty(householdNumber)){
            result.setFlag(false);
            result.setMsg("户号为空");
            return result;
        }
        List<LandEnclosure> list = landEnclosureService.findByHouseholdNumber(householdNumber);
        result.setFlag(true);
        result.setMsg("查询成功");
        result.setData(list);
        return result;
    }











   /*******************************************************私有方法*********************************************************************/

   private String checkSaveForm(String userId,MultipartFile[] files,List<LandEnclosure> landEnclosureList){
       if (StringUtil.isEmpty(userId)){
           return "用户主键为空";
       }
       if (files == null || files.length == 0){
           return "没有上传文件";
       }
       if (landEnclosureList == null || landEnclosureList.size() == 0){
           return "没有上传文件信息";
       }
       if (files.length != landEnclosureList.size()){
           return "文件数量与文件信息数量不一致";
       }
       for (LandEnclosure landEnclosure:landEnclosureList){
           if (StringUtil.isEmpty(landEnclosure.getFileType())){
               return "文件类型不能为空";
           }
           String checkFileType = DataTransformUtil.checkLandEnclosureFileType(landEnclosure.getFileType());
           if (checkFileType.equals("-1")){
               return "文件类型错误";
           }
       }
       return "校验通过";
   }


}
