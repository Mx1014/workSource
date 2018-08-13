// @formatter:off
package com.everhomes.sso;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

@Component
public class ZhenZhiHuiSSO{
    private static final String charset       = "UTF-8";
    private static final String APPENCKEY     = "50740c53204a658a";
    private static final String SSOServiceURL = "http://w1505m3190.iok.la:56535/ZHYQ/restservices/LEAPAuthorize/attributes/query?TICKET=";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZhenZhiHuiSSO.class);

    public void ssoservice(HttpServletRequest request, HttpServletResponse response)
    {
        String TICKET = request.getParameter("TICKET");

        if ( TICKET == null )
        {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "TICKET is null.");
        }

        try
        {
            //使用独有的身份认证时颁发的密钥解密数据
            TICKET = CBCDecrypt(TICKET, APPENCKEY);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        if ( TICKET == null )
        {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "TICKET is null.");
        }

        try
        {
            //使用attributes接口从SSO服务器获取信息
            String ret = requestService(SSOServiceURL + TICKET);
            if ( ret != null )
            {
                //使用独有密钥解密数据
                ret = CBCDecrypt(ret, APPENCKEY);
                Hashtable<String, String> bean = toJSON(ret);
                if ( bean != null )
                {
                    //目标用户名称
                    String user = bean.get("target_user");
                    //方法名
                    String method = bean.get("target_method");
                    //参数
                    String parameter = bean.get("parameter");
                    if(null != parameter){
                        String parameterval = new String( new BASE64Decoder().decodeBuffer(parameter) ,charset);
                        LOGGER.info("user = {}, method = {}, parameter = {}, parameterval = {}",user,method,parameter,parameterval);
                    }
                    //TODO 拿到用户和请求访问的注册资源地址后，实现登录和响应及重定向客户端请求等操作
                    //TODO 授权接入系统原有会话维持机制不需要改变，短期内再次接入的用户，可对比自身机制内用户会话缓存

                    /*//申请访问服务的示例
                    //格式为JSON格式,请自行解析,toJSON方法不能解析
                    if(parameter != null)
                        parameter = new String( new BASE64Decoder().decodeBuffer(parameter) ,charset);

                    //-~~~~~~~~调用 业务方法:method , 参数:parameter , 用户:user
                    String methodret = "[{\"lwfp_entry_eventname\":\"XXXXXX待办\",\"lwfp_step_createtime\":\"2015-08-01\"},{\"lwfp_entryid\":\"ce4fa478a2a645ad982f3050b8da4695\",\"lwfp_entry_eventname\":\"XXXXXX待办\",\"lwfp_step_createtime\":\"2015-08-02\"}]";
                    //返回业务方法的返回值
                    response.getWriter().write( new BASE64Encoder().encode( methodret.getBytes(charset)) );

                    //调用业务服务不需要保持session,这里判断假如当前有session则使session失效
                    HttpSession session = request.getSession(false);
                    if(session != null)
                        //使session失效
                        session.invalidate();
                    return;*/
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String requestService ( String url )
    {

        HttpURLConnection httpURLConnection = null;
        InputStream input = null;
        OutputStream output = null;

        try
        {
            URL urlObj = new URL(url);
            httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Accept-Language", "zh-cn");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)");
            httpURLConnection.setRequestProperty("Accept", "*/*");

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.connect();

            input = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            output = new BufferedOutputStream(byteStream);

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = input.read(buffer)) >= 0)
            {
                output.write(buffer, 0, count);
            }

            input.close();
            input = null;
            output.flush();
            output.close();
            output = null;

            return new String(byteStream.toByteArray(), "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if ( input != null )
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            if ( output != null )
            {
                try
                {
                    output.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if ( httpURLConnection != null )
                httpURLConnection.disconnect();
        }
        return null;
    }

    private static String CBCDecrypt ( String data , String sourcekey ) throws Exception
    {
        if ( sourcekey == null || sourcekey.length() != 16 )
        {
            throw new Exception("原始KEY必须为16字节字符串");
        }
        byte[] key = sourcekey.substring(0, 8).getBytes(charset);
        byte[] iv = sourcekey.substring(8).getBytes(charset);

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);

        // using DES in CBC mode
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 若采用NoPadding模式，data长度必须是8的倍数
        // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

        // 用密匙初始化Cipher对象
        IvParameterSpec param = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));

        return new String(decryptedData, charset);
    }

    private static Hashtable<String, String> toJSON ( String str )
    {
        BufferedReader sr = null;
        try
        {
            sr = new BufferedReader(new StringReader(str));
            String line = null;
            sr.readLine();
            Hashtable<String, String> ret = new Hashtable<String, String>();
            while ((line = sr.readLine()) != null)
            {
                if ( line.length() == 1 )
                    continue;
                int idx = line.indexOf(":");
                String name = line.substring(1, idx - 1);
                String val = null;
                if(line.endsWith(","))
                    val = line.substring(idx + 2, line.length() - 2);
                else val = line.substring(idx + 2, line.length() - 1);

                ret.put(name, val);
            }
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if ( sr != null )
                try
                {
                    sr.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return null;
    }

}
