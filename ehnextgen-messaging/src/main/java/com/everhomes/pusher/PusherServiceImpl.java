// @formatter:off
package com.everhomes.pusher;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.messaging.ApnsServiceFactory;
import com.everhomes.messaging.PushMessageResolver;
import com.everhomes.messaging.PusherService;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.rest.messaging.DeviceMessages;
import com.everhomes.rest.pusher.PushMessageCommand;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;
import com.everhomes.rest.user.UserLoginStatus;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserLogin;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.notnoop.apns.*;
import com.notnoop.exceptions.NetworkIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
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
        ApnsService service = this.certMaps.get(partner);
        if(service == null) {
            // init hear
            Cert cert = certProvider.findCertByName(partner);
            if(cert != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(cert.getData());
                //.withCert("/home/janson/projects/pys/apns/apns_develop.p12", "123456")
                ApnsServiceBuilder builder = APNS.newService().withCert(bis, cert.getCertPass().trim()).asPool(5);
                if(partner.indexOf("develop") >= 0) {
                    builder = builder.withSandboxDestination();
                } else {
                    builder = builder.withProductionDestination();
                        }
                ApnsService innerService = builder.build();
                if(innerService == null) {
                    LOGGER.warn("start apns server error");
                    return null;
                }
                service = new PriorityQueuedApnsService(innerService, Executors.defaultThreadFactory());
                service.start();
                
                ApnsService tmp = this.certMaps.putIfAbsent(partner, service);
                if(tmp != null) {
                    try{
                        service.stop();
                    } catch (Exception e) {
                    LOGGER.warn("stop apns server error");    
                    }
                    
                    service = tmp;
                }
            }
        }
        
        return service;
    }
    
    @Override
    public void stopApnsServiceByName(String partner) {
        ApnsService server = null;
        if(partner.startsWith(this.namespacePrefix)) {
            server = this.certMaps.remove(partner);
        } else if(partner.equals(this.certName)) {
            server = this.certMaps.remove(this.certName);
        }
        
        LOGGER.info("restarting apns service=" + partner );
        
        if(server != null) {
            server.stop();    
            }
    }
    
    private void pushMessageAndroid(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg, String platform, DeviceMessage devMessage) {
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
        pdu.setPlatform(platform);
        pdu.setNotification(msg.getContent());
        pdu.setMessageType("UNICAST");
        pdu.setMessageId(msgId);
        pdu.setDeviceId(destLogin.getDeviceIdentifier());

        long requestId = LocalSequenceGenerator.getNextSequence();
        borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
        
        LOGGER.info("pushing to uid=" + destLogin.getUserId() );
    }
    private void pushMessageApple(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg, String platform, DeviceMessage devMessage) {
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
//        if(devMessage.getAlertType().equals(DeviceMessageType.Jump.getCode())) {
//            payloadBuilder = payloadBuilder.customField("jumpObj", devMessage.getExtra().get("jumpObj"))
//                    .customField("jumpType", devMessage.getExtra().get("jumpType"));
//            }
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
        
//        String payload = APNS.newPayload().badge(3)
//                .customField("secret", "what do you think?")
//                .localizedKey("GAME_PLAY_REQUEST_FORMAT")
//                .localizedArguments("Jenna", "Frank")
//                .actionKey("Play").build();
        
//        if(msgId != 0) {
//            final Job job = new Job(PusherAction.class.getName(),
//                    new Object[]{ payload, identify, partner });
//            jesqueClientFactory.getClientPool().enqueue(queueName, job);    
//        } else {
        
        //use queue to notify
        
            int now =  (int)(new Date().getTime()/1000);
            boolean error = false;

            try {
                PriorityApnsNotification notification = new PriorityApnsNotification(EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
                                now + 60 * 60 /* Expire in one hour */,
                                identify /* Device Token */,
                                payload,
                                devMessage.getPriorigy());
                    ApnsService tempService = getApnsService(partner);
                    if(tempService != null) {
                            tempService.push(notification);   
                            if(LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Pushing message(push ios), pushMsgKey=" + partner + ", msgId=" + msgId + ", identify=" + identify
                                    + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
                                    }
                     } else {
                         LOGGER.warn("Pushing apnsServer not found");
                     }
            } catch (NetworkIOException e) {
                error = true;
                LOGGER.warn("apns error and stop it", e);
            } catch(Exception ex) {
                error = true;
                LOGGER.warn("apns error deviceId not correct", ex);
            }
            
            if(error) {
                try {
                    stopApnsServiceByName(partner);
                } catch(Exception ex) {
                    LOGGER.warn("stop apns service error", ex);
                }
                
            }
            
    }

    private String getPlatform(UserLogin destLogin) {
        if(destLogin.getStatus() == UserLoginStatus.LOGGED_OFF) {
            LOGGER.error("Pushing message, destLogin loggedoff, destLogin=" + destLogin);
            return null;
        }
        
        if(destLogin.getDeviceIdentifier() == null || destLogin.getDeviceIdentifier().isEmpty()) {
            return null;
        }

        Device d = this.deviceProvider.findDeviceByDeviceId(destLogin.getDeviceIdentifier());
        String platform = null;
        if(d == null) {
            LOGGER.warn("Pushing message, dest device not found, using auto detect, destLogin=" + destLogin);
            //auto detect by destLogin.getDeviceIdentifier()
            if(destLogin.getDeviceIdentifier().indexOf(":") >= 0) {
                platform = "android";
            } else if(destLogin.getDeviceIdentifier().length() >= 60) {
                platform = "iOS";
            }
            
            return platform;
        }
        
        platform = d.getPlatform();
        if(platform == null || !(platform.equals("iOS") || platform.equals("android"))) {
            //platform != iOS && platform != "android", auto detect by deviceId
            if(d.getDeviceId() != null) {
                if(d.getDeviceId().indexOf(":") >= 0) {
                    platform = "android";
                } else if(d.getDeviceId().length() >= 60) {
                    platform = "iOS";
                }
            }
        }
        
        return platform;
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

        String beanName = PushMessageResolver.PUSH_MESSAGE_RESOLVER_PREFIX + msg.getAppId();
        PushMessageResolver messageResolver = PlatformContext.getComponent(beanName);
        if(null == messageResolver) {
            messageResolver = PlatformContext.getComponent(PushMessageResolver.PUSH_MESSAGE_RESOLVER_DEFAULT);
        }
        //assert(messageResolver != null)
        DeviceMessage devMessage = messageResolver.resolvMessage(senderLogin, destLogin, msg);
        
        String platform = getPlatform(destLogin);
        if(platform == null) {
            return;
        }
        
        if(platform.equals("iOS")) {
            pushMessageApple(senderLogin, destLogin, msgId, msg, platform, devMessage);
        } else {
            //Android or other here
            pushMessageAndroid(senderLogin, destLogin, msgId, msg, platform, devMessage);
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
    public void createCert(Cert cert) {
        Cert tmp = this.certProvider.findCertByName(cert.getName());
        if(tmp != null) {
            this.certProvider.deleteCert(tmp);
        }
        
        this.certProvider.createCert(cert);
        
        this.stopApnsServiceByName(cert.getName());
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
            String msgStr = null;
            try {
                msgStr = mb.getContent();
                if(msgStr != null && !msgStr.isEmpty()) {
                    DeviceMessage msg = (DeviceMessage)StringHelper.fromJsonString(msgStr, DeviceMessage.class);
                    if(msg != null) {
                        deviceMsgs.add(msg);    
                        }
                    }
                
            } catch(Exception ex) {
                LOGGER.error("device message error msgStr=" + msgStr, ex);
            }
            
        }

        return deviceMsgs;
    }
    
    @Override
    public void pushServiceTest(PushMessageCommand cmd) {
        ApnsService service =
                APNS.newService()
                .withCert("/tmp/apns_appstore.p12", "zuolin")
                .withAppleDestination(false)
                .build();
        
//        ApnsService service = getApnsService("namespace:1000000");
        
        String payload = APNS.newPayload().alertBody(cmd.getMessage() + Math.random()).build();
        String token = cmd.getDeviceId();
        service.push(token, payload);
        
        Map<String, Date> inactiveDevices = service.getInactiveDevices();
        for (String deviceToken : inactiveDevices.keySet()) {
            Date inactiveAsOf = inactiveDevices.get(deviceToken);
            LOGGER.info("date=" + inactiveAsOf + " deviceToken=" + deviceToken);
        }
        
        //http://www.concretepage.com/java/jdk-8/java-8-completablefuture-example
//        List<Integer> list = Arrays.asList(10,20,30,40);
//
//        list.stream().map(data->CompletableFuture.supplyAsync(()->getNumber(data))).
//                map(compFuture->compFuture.thenApply(n->n*n)).map(t->t.join())
//                .forEach(s->System.out.println(s));
        
//        List<String> list = Arrays.asList("A","B","C","D");
//        list.stream().map(s->CompletableFuture.supplyAsync(()->s+s))
//                .map(f->f.whenComplete((result,error)->System.out.println(result+" Error:"+error))).count();
        
//        Map<String, Long> deviceMap = new HashMap<String, Long>();
//        deviceMap.put("7e978fbb0d127671e30b4704414c7bdf272b06d066fccde8a2f309bcfa110393", 0l);
//        deviceMap.put("frompython_195870_xiaoxiao2", 0l);
//        deviceMap = requestDevices(deviceMap);
//        
//        LOGGER.info("all device is: " + deviceMap);
    }
    
//    private static int getNumber(int a){
//        return a*a;
//    }
    
    @Override
    public Map<String, Long> requestDevices(Map<String, Long> deviceMap) {
        List<String> devs = new ArrayList<>();
        deviceMap.forEach((dev, t) -> devs.add(dev));
        
        List<Border> borders = this.borderProvider.listAllBorders();
        if(borders != null) {
            borders.stream().map((b) -> {
                BorderConnection connection = borderConnectionProvider.getBorderConnection(b.getId());
                return connection;
            }).map((conn) -> {
                DeviceRequestPdu pdu = new DeviceRequestPdu();
                pdu.setDevices(devs);
                return conn.requestDevice(pdu);
            }).map((t) -> {
                try {
                    return t.join();
                } catch(Exception e) {
                    LOGGER.warn("get request device error " + e.getMessage());
                    return null;
                }
                
            }).forEach((pdu) -> {
                
                if(pdu != null) {
                    for(int i = 0; i < pdu.getDevices().size(); i++) {
                        Long t = pdu.getLastValids().get(i);
                        if(!t.equals(0l)) {
                            deviceMap.put(pdu.getDevices().get(i), t);
                        }
                    }                    
                }

            });
        }
        
        return deviceMap;
    }
    
    /**
     * if not pushed in 10s, push it.
     */
    @Override
    public void checkAndPush(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg) {
        String key = getPushMessageKey(destLogin.getNamespaceId(), destLogin.getDeviceIdentifier()) + ":check";
        
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(key);
        redisTemplate.opsForValue().set(key, "1", 10, TimeUnit.SECONDS);
        
        String platform = getPlatform(destLogin);
        
        if(o == null && platform != null && platform.equals("iOS")) {
            //iOS only
            pushMessage(senderLogin, destLogin, msgId, msg);
        }
    }
}
