package com.everhomes.util.doc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DocUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocUtil.class);


    public Configuration configure=null;

    private HttpURLConnection httpUrl = null;
    
    public DocUtil(){
        configure=new Configuration(Configuration.VERSION_2_3_23);
        configure.setDefaultEncoding("utf-8");
    }
    /**
     * 根据Doc模板生成word文件
     * @param dataMap 需要填入模板的数据
     * @param downloadType 文件名称
     * @param savePath 保存路径
     */
    public void createDoc(Map<String,Object> dataMap,String downloadType,String savePath){
        LOGGER.info("createDoc：downloadType:{}, savePath:{}", downloadType, savePath);
        try {
            //加载需要装填的模板
            Template template=null;
            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
            //加载模板文件
            configure.setClassForTemplateLoading(this.getClass(), "/template");
//            configure.setClassForTemplateLoading(this.getClass(), "/com/everhomes/util/doc/template");
            //设置对象包装器
//            configure.setObjectWrapper(new DefaultObjectWrapper());
            //设置异常处理器
            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            //定义Template对象，注意模板类型名字与downloadType要一致
            template=configure.getTemplate(downloadType+".xml");
            File outFile=new File(savePath);
            if (outFile.exists()) {
                LOGGER.info("文件存在：{}", savePath);
            } else {
                LOGGER.info("文件不存在，正在创建...：{}", savePath);
                if (outFile.createNewFile()) {
                    LOGGER.info("文件创建成功！：{}", savePath);
                } else {
                    LOGGER.info("文件创建失败！：{}", savePath);
                }
            }
            Writer out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
//            out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            template.process(dataMap, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 根据Doc模板生成word文件
//     * @param dataMap 需要填入模板的数据
//     * @param downloadType 文件名称
//     * @param savePath 服务器路径
//     */
//    public void createDocOnServer(Map<String,Object> dataMap,String downloadType,String savePath){
//        LOGGER.info("createDocOnServer：downloadType:{}, savePath:{}", downloadType, savePath);
//        try {
//            //加载需要装填的模板
//            Template template=null;
//            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
//            //加载模板文件
////            this.getClass().getResourceAsStream("/excels/pmtask.xlsx");
////            configure.setClassForTemplateLoading(this.getClass(), "/com/everhomes/util/doc/template");
//            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@"+this.getClass().getResource("/").getPath());
//            configure.setClassForTemplateLoading(this.getClass(), "/template");
//
//            //设置对象包装器
////            configure.setObjectWrapper(new DefaultObjectWrapper());
//            //设置异常处理器
//            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
//            //定义Template对象，注意模板类型名字与downloadType要一致
//            template=configure.getTemplate(downloadType+".xml");
//
//            File outFile=new File(savePath);
//            if (outFile.exists()) {
//                LOGGER.info("文件存在：{}", savePath);
//            } else {
//                LOGGER.info("文件不存在，正在创建...：{}", savePath);
//                if (outFile.createNewFile()) {
//                    LOGGER.info("文件创建成功！：{}", savePath);
//                } else {
//                    LOGGER.info("文件创建失败！：{}", savePath);
//                }
//            }
//            Writer outWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
//            template.process(dataMap, outWriter);
//            outWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }
//    }
    
    public String getImageStr(String imgFile){
        InputStream in=null;
        byte[] data=null;
        try {
            in=new FileInputStream(imgFile);
            data=new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {                       
            e.printStackTrace();
        }
        BASE64Encoder encoder=new BASE64Encoder();
        return encoder.encode(data);
    }
    
    public String getUrlImageStr(String destUrl){
    	InputStream inputStream = saveToFile(destUrl);
    	String str = GetImageStrByInput(inputStream);
//    	httpUrl.disconnect();
    	return str;
    }

    /**
     * 从URL中读取图片,转换成流形式.
     * @param destUrl
     * @return
     */
    public InputStream saveToFile(String destUrl){

        URL url = null;
        InputStream in = null;
        try{
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            in = httpUrl.getInputStream();
            return in;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 读取输入流,转换为Base64字符串
//     * @param input
//     * @return
//     */
//    public String GetImageStrByInput(InputStream input) {
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            data = new byte[input.available()];
//            input.read(data);
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
//    }

    /**
     * 读取输入流,转换为Base64字符串
     * @param input
     * @return
     */
    public String GetImageStrByInput(InputStream input) {
        byte[] data = new byte[0];
        // 读取图片字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] read = new byte[1024];
            int len;
            while ((len = input.read(read)) != -1) {
                bos.write(read, 0, len);
                read = new byte[1024];
            }
            data = bos.toByteArray();
            input.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }


    public void closeHttpConn(){
        httpUrl.disconnect();
    }
}

