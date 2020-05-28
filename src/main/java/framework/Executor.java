package framework;

import constant.Sys;
import enm.RESULT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.DateUtil;
import util.FileUtils;
import util.StringUtil;
import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//异步执行的任务
@Component
public class Executor {



    //初始化
    public static Executor serviceUtil;
    @PostConstruct
    public void init() {
        serviceUtil = this;
    }

    private ExecutorService executor = Executors.newFixedThreadPool(20) ;


    /**
     * 删除单个文件
     * @param filePath 文件绝对路径
     * @throws Exception
     */
    public void delFile(String filePath) throws Exception {
        if (StringUtil.isEmpty(filePath)){
            return;
        }
        executor.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    FileUtils.delete(filePath);
                }catch(Exception e) {
                    throw new RuntimeException("文件删除子任务出错");
                }
            }
        });
    }







    /**********************************私有方法****************************************/

}
