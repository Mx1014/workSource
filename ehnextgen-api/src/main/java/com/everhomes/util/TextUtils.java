package com.everhomes.util;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;  
import java.util.ArrayList;  
import java.util.List;  
  
/**
 * 内容导出到txt
 * @author 黄明波
 *
 */
public class TextUtils {  
  
    public static boolean exportTxt(File file, List<String> dataList,String heads){  
        FileOutputStream out=null;  
         try {  
            out = new FileOutputStream(file);  
         return exportTxtByOS(out, dataList, heads);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return false;  
        }  
          
    }  
      
    /** 
     * 导出 
     * @param out 输出流 
     * @param dataList  数据 
     * @param heads  表头 
     * @return 
     * @author liuweilong@zhicall.com   
     * @create 2016-4-27 上午9:49:49 
     */  
     public static boolean exportTxtByOS(OutputStream out, List<String> dataList,String heads){  
            boolean isSucess=false;  
              
            OutputStreamWriter osw=null;  
            BufferedWriter bw=null;  
            try {  
                osw = new OutputStreamWriter(out);  
                bw =new BufferedWriter(osw);  
                //循环表头  
                if(heads!=null&&!heads.equals("")){  
                     bw.append(heads).append("\r");  
                }  
                //循环数据  
                if(dataList!=null && !dataList.isEmpty()){  
                    for(String data : dataList){  
                        bw.append(data).append("\r");  
                    }  
                }  
                isSucess=true;  
            } catch (Exception e) {  
                e.printStackTrace();  
                isSucess=false;  
            }finally{  
                if(bw!=null){  
                    try {  
                        bw.close();  
                        bw=null;  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }   
                }  
                if(osw!=null){  
                    try {  
                        osw.close();  
                        osw=null;  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }   
                }  
                if(out!=null){  
                    try {  
                        out.close();  
                        out=null;  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }   
                }  
            }  
              
            return isSucess;  
        }  
  
  
       
    /** 
     *  测试  
     * @param args 
     * @author liuweilong@zhicall.com   
     * @create 2016-4-27 上午10:11:46 
     */  
     public static void main(String[] args) {  
         //导出数据测试  
       List<String> dataList=new ArrayList<String>();  
          dataList.add("1,张三,男");  
          dataList.add("2,李四,男");  
          dataList.add("3,小红,女");  
          File file = new File("E:/test");  
          if(!file.exists()){  
              file.mkdir();  
          }  
          boolean isSuccess=exportTxt(new File("F:/tmp/20180502/export/bobo.txt"), dataList,"编码,姓名,性别");  
          System.out.println(isSuccess);  
              
           
    }  
       
}  