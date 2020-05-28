package service;


import enm.RESULT;
import model.LandEnclosure;
import model.LandRightsInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/***
 * 宗地附件Service
 */
public interface ILandEnclosureService {
    /**
     * 通过主键查询宗地附件信息
     * @param id 主键
     * @return
     */
    LandEnclosure get(String id);

    /**
     * 保存宗地附件文件
     * @param fileType 文件类型（含前缀序号）
     * @param file 文件
     * @param landRightsInfo 宗地权籍信息
     * @return savePath 保存路径,保存失败返回null
     */
    String saveFile(String fileType,MultipartFile file, LandRightsInfo landRightsInfo);

    /**
     * 保存宗地附件信息
     * @param householdNumber 户号
     * @param webPath 宗地附件保存相对路径
     * @param userId 用户主键
     * @param presentTime 当前系统时间
     * @param file 文件
     * @param landEnclosure 宗地附件信息
     * @return
     */
    RESULT saveLandEnclosure(String householdNumber,String webPath,String userId,Date presentTime,MultipartFile file,LandEnclosure landEnclosure);

    /**
     * 删除宗地附件信息
     * @param id 宗地附件信息主键
     * @return
     */
    RESULT delLandEnclosure(String id);

    /**
     * 更新宗地附件信息
     * @param userId 用户主键
     * @param id 附件主键
     * @param presentTime 当前时间
     * @param webPath 附件相对路径
     * @param file 文件
     * @return
     */
    RESULT updLandEnclosure(String userId,String id,Date presentTime,String webPath,MultipartFile file);

    /**
     * 根据户号查询所有附件信息
     * @param householdNumber 户号
     * @return
     */
    List<LandEnclosure> findByHouseholdNumber(String householdNumber);
}

