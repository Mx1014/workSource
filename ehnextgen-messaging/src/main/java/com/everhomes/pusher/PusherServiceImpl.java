// @formatter:off
package com.everhomes.pusher;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.ApnsServiceFactory;
import com.everhomes.messaging.NotifyMessage;
import com.everhomes.messaging.PushMessageResolver;
import com.everhomes.messaging.PusherService;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.rest.messaging.DeviceMessageType;
import com.everhomes.rest.messaging.DeviceMessages;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;
import com.everhomes.rest.user.UserLoginStatus;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.google.gson.Gson;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;

@Component
public class PusherServiceImpl implements PusherService, ApnsServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherServiceImpl.class);
    
    @Autowired
    private BorderConnectionProvider borderConnectionProvider;

    @Autowired
    private MessageBoxProvider messageBoxProvider;

    @Autowired
    CertProvider certProvider;

    @Autowired
    DeviceProvider deviceProvider;
    
    @Autowired
    ConfigurationProvider configProvider;
    
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private String queueName = "iOS-pusher2";

    ApnsService service = null;

    String messageBoxPrefix = "PUSH_MSG:";

    @Value("${apns.certname}")
    String certName = "apns-develop";
    
    private String namespacePrefix = "namespace:";
    
    ConcurrentMap<String, ApnsService> certMaps = new ConcurrentHashMap<String, ApnsService>();

    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }
    
    @Override
    public ApnsService getApnsService(String partner) {
//        String partner = certName;
//        if(null != inPartner && !inPartner.isEmpty()) {
//            partner = inPartner;
//        }
        
        ApnsService service = this.certMaps.get(partner);
        if(service == null) {
            // init hear
            Cert cert = certProvider.findCertByName(partner);
            if(cert != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(cert.getData());
                //.withCert("/home/janson/projects/pys/apns/apns_develop.p12", "123456")
                ApnsServiceBuilder builder = APNS.newService().withCert(bis, cert.getCertPass());
                if(certName.indexOf("develop") >= 0) {
                    builder = builder.withSandboxDestination();
                } else {
                    builder = builder.withProductionDestination();
                        }
                service = builder.build();
                ApnsService tmp = this.certMaps.putIfAbsent(partner, service);
                if(tmp != null) {
                    service = tmp;
                }
            }
        }
        
        return service;
    }
    
    //https://developer.apple.com/library/
    //  ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/ApplePushService.html
    //  #//apple_ref/doc/uid/TP40008194-CH100-SW1
    public void pushMessage(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg) {
        if(null == destLogin.getDeviceIdentifier()) {
            LOGGER.error("Pushing message, destLogin deviceId is null, msgId=" + msgId 
                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
            return;
        }

        if(destLogin.getStatus() == UserLoginStatus.LOGGED_OFF) {
            LOGGER.error("Pushing message, destLogin loggedoff, msgId=" + msgId 
                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
            return;
        }

        Device d = this.deviceProvider.findDeviceByDeviceId(destLogin.getDeviceIdentifier());
        if(d == null) {
            LOGGER.error("Pushing message, dest device not found, msgId=" + msgId 
                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
            return;
        }
        String beanName = PushMessageResolver.PUSH_MESSAGE_RESOLVER_PREFIX + msg.getAppId();
        PushMessageResolver messageResolver = PlatformContext.getComponent(beanName);
        if(null == messageResolver) {
            messageResolver = PlatformContext.getComponent(PushMessageResolver.PUSH_MESSAGE_RESOLVER_DEFAULT);
            }
        //assert(messageResolver != null)
        DeviceMessage devMessage = messageResolver.resolvMessage(senderLogin, destLogin, msg);
        
        if(d.getPlatform().equals("iOS")) {
                PayloadBuilder payloadBuilder = APNS.newPayload();
                if(devMessage.getAlert().length() > 20) {
                    payloadBuilder = payloadBuilder.alertBody(devMessage.getAlert().substring(0, 20));
                } else {
                    payloadBuilder = payloadBuilder.alertBody(devMessage.getAlert());
                    }
                
                payloadBuilder = payloadBuilder
                //.alertAction(devMessage.getAction())
                //.actionKey("testAction")
                //.category("testCategory")
                .alertTitle(devMessage.getTitle())
                .badge(devMessage.getBadge())
                //.forNewsstand() aps {content-available: 1}
                //.instantDeliveryOrSilentNotification()
                .customField("alertType", devMessage.getAlertType())
                .customField("appId", devMessage.getAppId());
                
                if(devMessage.getAudio() != null && !devMessage.getAudio().isEmpty()) {
                    payloadBuilder = payloadBuilder.sound(devMessage.getAudio());    
                    }
//                if(devMessage.getAlertType().equals(DeviceMessageType.Jump.getCode())) {
//                    payloadBuilder = payloadBuilder.customField("jumpObj", devMessage.getExtra().get("jumpObj"))
//                            .customField("jumpType", devMessage.getExtra().get("jumpType"));
//                    }
                if(null != devMessage.getAction()) {
                    payloadBuilder = payloadBuilder.customField("actionType", devMessage.getAction())
                          .customField("actionData", devMessage.getExtra().get("actionData"));
                    }
                
                String payload = payloadBuilder.build();
                
                //.customField("", devMessage.)
                //.customField("", devMessage.)
                //String payload = APNS.newPayload().alertBody(devMessage.getAlert()).build();
                //String identify = "0e45353318a46f03269fbce18f6643475043d01d298ec4ef305a70b7c1de09ff";
                //String identify = "b135d649736eedd8dbf649a245a42856d400d13fbf96ecc0a2746fb670f09471";
                String identify = destLogin.getDeviceIdentifier();
                identify = identify.replace("<", "").replace(">", "").replace(" ", "");
                String partner = certName;
                if(destLogin.getNamespaceId() > 0) {
                    partner = this.namespacePrefix + destLogin.getNamespaceId();
                    }
                if(destLogin.getPusherIdentify() != null) {
                    partner = partner + ":" + destLogin.getPusherIdentify(); 
                    }
                
//                String payload = APNS.newPayload().badge(3)
//                        .customField("secret", "what do you think?")
//                        .localizedKey("GAME_PLAY_REQUEST_FORMAT")
//                        .localizedArguments("Jenna", "Frank")
//                        .actionKey("Play").build();
                final Job job = new Job(PusherAction.class.getName(),
                        new Object[]{ payload, identify, partner });
                jesqueClientFactory.getClientPool().enqueue(queueName, job);
            
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pushing message(push ios), pushMsgKey=" + partner + ", msgId=" + msgId 
                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
            }
        } else {
            //Android or other hear
            //copy the message to message box
            //messageBoxProvider.putMessage(messageBoxPrefix+destLogin.getDeviceIdentifier(), msgId);
            
            Message message = new Message();
            message.setAppId(AppConstants.APPID_DEFAULT);
            //message.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
            message.setNamespaceId(destLogin.getNamespaceId());
            message.setChannelType("");
            message.setChannelToken("");
            message.setSenderUid(senderLogin.getUserId());
            
            Gson gson = new Gson();
            message.setContent(gson.toJson(devMessage));
            String key = getPushMessageKey(destLogin.getNamespaceId(), destLogin.getDeviceIdentifier());
            this.messageBoxProvider.putMessage(key, message);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pushing message(push android), pushMsgKey=" + key + ", msgId=" + msgId 
                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
            }

            PusherNotifyPdu pdu = new PusherNotifyPdu();
            pdu.setPlatform(d.getPlatform());
            pdu.setNotification(msg.getContent());
            pdu.setMessageType("UNICAST");
            pdu.setMessageId(msgId);
            pdu.setDeviceId(destLogin.getDeviceIdentifier());

            long requestId = LocalSequenceGenerator.getNextSequence();
            borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
            
            LOGGER.info("pushing to uid=" + destLogin.getUserId() );
        }
    }
    
    private String getPushMessageKey(Integer namespaceId, String deviceId) {
        if(namespaceId == null || namespaceId.equals(0)) {
            return messageBoxPrefix + ":" + deviceId; 
        } else {
            return messageBoxPrefix + ":" + namespaceId + ":" + deviceId;
        }
    }

    @Override
    public void pushMessage(NotifyMessage msg) {
        if(null == msg.getDeviceId()) {
            LOGGER.error("The deviceId is null");
            return;
        }
        Device d = deviceProvider.findDeviceByDeviceId(msg.getDeviceId());
        if(d == null) {
            LOGGER.error("Cannot find such device: " + msg.getDeviceId());
            return;
        }
        if(d.getPlatform().equals("iOS")) {
            ApnsService tempService = this.getApnsService(certName);
          if(tempService != null) {
              String payload = APNS.newPayload().alertBody(msg.getMessage()).build();
              //String token = "b135d649736eedd8dbf649a245a42856d400d13fbf96ecc0a2746fb670f09471";
              tempService.push(msg.getDeviceId(), payload);
    
    
    //          Map<String, Date> inactiveDevices = service.getInactiveDevices();
    //          for (String deviceToken : inactiveDevices.keySet()) {
    //              Date inactiveAsOf = inactiveDevices.get(deviceToken);
    //              System.out.println(inactiveAsOf);
    //          }
      }
        } else {
            //Android hear

              Message m = new Message();
              m.setContent(msg.getMessage());
              m.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
              m.setAppId(AppConstants.APPID_PUSH);
              long msgId = messageBoxProvider.putMessage(messageBoxPrefix+msg.getDeviceId(), m);

              PusherNotifyPdu pdu = new PusherNotifyPdu();
              pdu.setPlatform(d.getPlatform());
              pdu.setNotification(msg.getMessage());
              pdu.setMessageType("UNICAST");
              pdu.setMessageId(msgId);
              pdu.setDeviceId(msg.getDeviceId());

              long requestId = LocalSequenceGenerator.getNextSequence();
              borderConnectionProvider.broadcastToAllBorders(requestId, pdu);

        }


//        Message message;
//        message.setAppId(appId);
//        message.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
//        long msgId = messageBoxProvider.createMessage(message);
//        message.setMessageSequence(msgId);
//
//        message.addMetaParam("aaa", "bbb");
//        messageBoxProvider.putMessage(messageBoxKey, message);
//
//        String messageBoxKey = "borderid:1";
//        messageBoxProvider.putMessage(messageBoxKey, msgId);
//
//        messageBoxProvider.putMessage("ab:1", msgId);
//        MessageLocator l = new MessageLocator(messageBoxKey);
//        messageBoxProvider.getPastToRecentMessages(arg0, arg1);
//        messageBoxProvider.removeMessage(arg0, arg1);

        long requestId = LocalSequenceGenerator.getNextSequence();
        PusherNotifyPdu pdu = new PusherNotifyPdu();
        pdu.setPlatform("android");
        pdu.setNotification("hello");
        pdu.setMessageType("BROADCAST");
        pdu.setMessageId(1);

        borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
    }

//    @Override
//    public void pushMessages(List<NotifyMessage> messages) {
//    }
    
    @Override
    public void createCert(Cert cert) {
        Cert tmp = this.certProvider.findCertByName(cert.getName());
        if(tmp != null) {
            this.certProvider.deleteCert(tmp);
            }
        
        this.certProvider.createCert(cert);
        if(cert.getName().startsWith(this.namespacePrefix)) {
            this.certMaps.remove(cert.getName());
        } else if(cert.getName().equals(this.certName)) {
            this.certMaps.remove(this.certName);    
        }
    }

    @Override
    public DeviceMessages getRecentMessages(RecentMessageCommand cmd) {
        //UserLogin userLogin = UserContext.current().getLogin();
        
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getCount());
        String key = getPushMessageKey(cmd.getNamespaceId(), cmd.getDeviceId());
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pushing message(fetch), pushMsgKey=" + key + ", cmd=" + cmd + ", namespaceId=" + cmd.getNamespaceId());
        }
        
        MessageLocator l = new MessageLocator(key);
        l.setAnchor(cmd.getAnchor());

        List<Message> msgInBox = messageBoxProvider.getPastToRecentMessages(l, count, true);
        DeviceMessages deviceMsgs = new DeviceMessages();
        deviceMsgs.setAnchor(l.getAnchor());

        for(Message mb : msgInBox) {
            Gson gson = new Gson();
            DeviceMessage msg = gson.fromJson(mb.getContent(), DeviceMessage.class);
            if(msg != null) {
                deviceMsgs.add(msg);    
                }
        }

        return deviceMsgs;
    }
}
