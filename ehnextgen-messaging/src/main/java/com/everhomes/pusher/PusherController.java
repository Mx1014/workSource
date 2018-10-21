package com.everhomes.pusher;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.NotifyMessage;
import com.everhomes.messaging.PusherService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.device.CreateCertCommand;
import com.everhomes.rest.device.DeviceDTO;
import com.everhomes.rest.device.RegistDeviceCommand;
import com.everhomes.rest.messaging.DeviceMessages;
import com.everhomes.rest.pusher.PushMessageCommand;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.rest.user.SendMessageTestCommand;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;

/**
 * <ul>
 * <li><p>消息推送原理</p>
 * 
 * <ul>
 * <li>客户端推送 使用 Client Service 长期与 Border Server 保持连接</li>
 * <li>Core Server 需要推送消息，先将消息存入 MessageBox</li>
 * <li>Core 给 Border Server 一个控制消息</li>
 * <li>Border Server 将控制消息转发给相应的客户端</li>
 * <li>客户端通过 WebSocket，从 Border Server 获取推送消息</li>
 * <li>Border Server 将请求转给 Core Server，同时将 Border Server 的回复转回给 Client Service</li>
 * </ul></li>
 * <li><p>需要调用接口</p>
 * 
 * <ul>
 * <li>{@link #recentMessages(String, Long)}</li>
 * </ul></li>
 * <li><p>WebSocket 注意细节</p>
 * 
 * <ul>
 * <li>第一次连接需要进行HANDSHAKE，HANDSHAKE过程会注册DeviceId等</li>
 * <li>Client Service 可以根据需要，控制 ping 消息的次数。从而达到节电的效果</li>
 * <li>为了 Client Service 尽可能减少内存消耗，只需要维护一条 WebSocket 的长连接</li>
 * <li>获取消息通过走 WebSocket，从而避免了 Http 请求，减化实现，减少消耗</li>
 * </ul></li>
 * </ul>
 * 
 * @author janson
 *
 */
@RestDoc(value="Push notification controller", site="messaging")
@RestController
@RequestMapping("/pusher")
public class PusherController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherController.class);
    
    @Autowired
    PusherService pusherService;

    @Autowired
    DeviceProvider deviceProvider;

//    @Autowired
//    CertProvider certProvider;
    
    @Autowired
    BorderProvider borderProvider;
    
    @Autowired
    UserService userService;

    /**
     * <b>URL: /pusher/createCert</b>
     * <p>创建密钥</p>
     * @param 密钥名字
     * @param 密钥类型
     * @param 密钥对应的文件
     * @return 成功与否
     */
    @RequestMapping("createCert")
    @RestReturn(value=String.class)
    public RestResponse createCert(@Valid CreateCertCommand certCommand,
            @RequestParam(value = "attachment_file_", required = true) MultipartFile[] files) {
        RestResponse response = new RestResponse();
        if(files.length == 0) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("File not found");
            return response;
        }

        Cert cert = new Cert();
        cert.setName(certCommand.getName());
        cert.setCertType(certCommand.getCertType());
        cert.setCertPass(certCommand.getCertPass());
        try {
            cert.setData(files[0].getBytes());
            this.pusherService.createCert(cert);
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");
        } catch (IOException e) {
            response.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            response.setErrorDescription("Cannot read file");
        }

        return response;
    }

    /**
     * <b>URL: /pusher/registDevice</b>
     * <p>注册设备</p>
     */
    @RequestMapping("registDevice")
    //@RequireAuthentication(false)
    @RestReturn(DeviceDTO.class)
    public RestResponse registDevice(@Valid RegistDeviceCommand cmd) {
        Device device;
        
        if(null == cmd.getDeviceId()) {
            LOGGER.error("registDevice error, deviceId is null");
            return new RestResponse(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "deviceId is null");
        }
        device = this.deviceProvider.findDeviceByDeviceId(cmd.getDeviceId());
        if(device == null) {
            device = new Device();
            device.setDeviceId(cmd.getDeviceId());
            device.setPlatform(cmd.getPlatform());
            device.setProduct(cmd.getProduct());
            device.setBrand(cmd.getBrand());
            device.setDeviceModel(cmd.getDeviceModel());
            device.setSystemVersion(cmd.getSystemVersion());
            device.setMeta(cmd.getMeta());
           
            //add by huanglm IOS推送升级需在注册设备时多传两个参数
            device.setBundleId(cmd.getBundleId());
            device.setPusherServiceType(cmd.getPusherServiceType());
            
            this.deviceProvider.registDevice(device);
            
            //用于开发阶段服务器后台打印信息，最后要注掉
           // LOGGER.error("registDevice create new DeviceId:"+cmd.getDeviceId()+";BundleId:"+cmd.getBundleId());
        }
        //else if(StringUtils.isBlank(device.getBundleId())||StringUtils.isBlank(device.getPusherServiceType()))
        else{
        	//add by huanglm IOS推送升级需在注册设备时多传两个参数
            device.setBundleId(cmd.getBundleId());
            device.setPusherServiceType(cmd.getPusherServiceType());
            
            this.deviceProvider.updateDevice(device);
          //用于开发阶段服务器后台打印信息，最后要注掉
          //  LOGGER.error("registDevice update new DeviceId:"+cmd.getDeviceId()+";BundleId:"+cmd.getBundleId());
        }
      //用于开发阶段服务器后台打印信息，最后要注掉
       // LOGGER.error("registDevice  DeviceId:"+cmd.getDeviceId()+";BundleId:"+cmd.getBundleId());
        return new RestResponse(ConvertHelper.convert(device, DeviceDTO.class));
    }

    /**
     * <b>URL: /pusher/push</b>
     * <p>发送推送消息。目前仅用与测试</p>
     */
    @RequestMapping("push")
    @RestReturn(value=String.class)
    public RestResponse pushMessage(@Valid PushMessageCommand cmd) {
        RestResponse response = new RestResponse();
        
//        NotifyMessage msg = new NotifyMessage();
//        msg.setDeviceId(cmd.getDeviceId());
//        msg.setMessage(cmd.getMessage());
//        pusherService.pushMessage(msg);
        
        pusherService.pushServiceTest(cmd);
        
//        SendMessageTestCommand cmd2 = new SendMessageTestCommand();
//        cmd2.setUserId(195870l);
//        cmd2.setLoginId(5);
//        cmd2.setNamespaceId(1000000);
//        userService.pushMessageTest(cmd2);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pusher/recentMessages</b>
     * <p>获取推送的消息</p>
     * @param deviceId 设备ID
     * @param anchor 消息游标信息
     * @return
     */
    @RequestMapping("recentMessages")
    @RestReturn(value=DeviceMessages.class)
    public RestResponse recentMessages(@Valid RecentMessageCommand cmd) {

        DeviceMessages msgs = pusherService.getRecentMessages(cmd);
        RestResponse response = new RestResponse(msgs);
        return response;
    }


    /**
     * <b>URL: /pusher/flushApnsClientCache</b>
     * <p>在数据库修改推送证书的时候要清掉内在中的旧推送客户端对象</p>
     */
    @RequestMapping("flushApnsClientCache")
    @RestReturn(value=String.class)
    public RestResponse flushApnsClientCache() {
        pusherService.flushHttp2ClientMaps();
        RestResponse response = new RestResponse();
        return response;
    }


    private List<String> listAllBorderAccessPoints() {
        List<Border> borders = this.borderProvider.listAllBorders();
        return borders.stream().map((Border border) -> {
           return String.format("%s:%d", border.getPublicAddress(), border.getPublicPort()); 
        }).collect(Collectors.toList());
    }
    
    /**msgs
     * <b>URL: /pusher/serverList</b>
     * 获取推送服务器列表
     * @return
     */
    @RequestMapping("serverList")
    @RequireAuthentication(false)
    @RestReturn(value=String.class, collection=true)
    public RestResponse borderList() {
        List<String> borderList = this.listAllBorderAccessPoints();
        RestResponse response = new RestResponse(borderList);
        return response;
    }

}
