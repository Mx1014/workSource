package com.everhomes.pusher;

        import com.everhomes.appurl.AppUrlService;
        import com.everhomes.cert.Cert;
        import com.everhomes.cert.CertProvider;
        import com.everhomes.device.DeviceProvider;
        import com.everhomes.messaging.PusherVender;
        import com.everhomes.messaging.PusherVenderType;
        import com.everhomes.messaging.PusherVendorData;
        import com.everhomes.messaging.PusherVendorService;
        import com.everhomes.rest.appurl.AppUrlDTO;
        import com.everhomes.rest.appurl.GetAppInfoCommand;
        import com.everhomes.rest.messaging.DeviceMessage;
        import com.everhomes.rest.user.OSType;
        import com.everhomes.user.UserLogin;
        import com.everhomes.util.StringHelper;
        import com.everhomes.util.ThreadUtil;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

        import java.util.concurrent.ConcurrentHashMap;
        import java.util.concurrent.ConcurrentMap;
        import java.util.concurrent.ScheduledExecutorService;

@Component
public class PusherVendorServiceImpl implements PusherVendorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherVendorServiceImpl.class);

    @Autowired
    CertProvider certProvider;

    @Autowired
    DeviceProvider deviceProvider;

    @Autowired
    AppUrlService appUrlService;

    ConcurrentMap<String, PusherVender> pusherMap = new ConcurrentHashMap<String, PusherVender>();
    private static ScheduledExecutorService schExecutor = ThreadUtil.newScheduledExecutorService(6, "pushervendor");

    @Override
    public void pushMessageAsync(PusherVenderType venderType, UserLogin senderLogin, UserLogin destLogin, com.everhomes.msgbox.Message msg, DeviceMessage devMessage) {
        final PusherVendorService srv = this;
        schExecutor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    srv.pushMessage(venderType, senderLogin, destLogin, msg, devMessage);
                } catch(Exception ex) {
                    LOGGER.error("Pushing submit task error venderType=" + venderType);
                    LOGGER.error("===== ex = {}",ex);
                }

            }

        });
    }

    @Override
    public void pushMessage(PusherVenderType venderType, UserLogin senderLogin, UserLogin destLogin, com.everhomes.msgbox.Message msg, DeviceMessage devMessage) {
        int namespaceId = destLogin.getNamespaceId();
        String name = String.format("namespace:%d:%s", namespaceId, venderType.getCode());
        PusherVender pusher = pusherMap.get(name);

        LOGGER.debug("================= venderType={},namespaceId={}",venderType,namespaceId);

        GetAppInfoCommand cmd = new GetAppInfoCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setOsType(OSType.Android.getCode());
        AppUrlDTO dto = appUrlService.getAppInfo(cmd);
        if(dto != null) {
            devMessage.setTitle(dto.getName());
            devMessage.setIcon(dto.getLogoUrl());
        }

        if(pusher == null) {
            Cert cert = certProvider.findCertByName(name);
            if (cert == null || cert.getData() == null) {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Pushing name= " + name + " not find");
                    return;
                }
            }

            String json = new String(cert.getData());
            PusherVendorData data = (PusherVendorData)StringHelper.fromJsonString(json, PusherVendorData.class);

            LOGGER.debug("======json={},cert={},PusherVendorData={}",json,cert,data);

            if(venderType == PusherVenderType.HUAWEI) {
                if(data == null || data.getAppId() == null || data.getAppSecret() == null || data.getAppPkgName() == null) {
                    if(LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Pushing name= " + name + " data json error");
                        return;
                    }
                }

                pusher = new PusherHuawei(data.getAppId(), data.getAppSecret(), data.getAppPkgName());
            } else if (venderType == PusherVenderType.XIAOMI) {
                if(data == null || data.getAppSecret() == null || data.getAppPkgName() == null) {
                    LOGGER.warn("Pushing name= " + name + " data json error");
                    return;
                }

                pusher = new PusherXiaomi(data.getAppSecret(), data.getAppPkgName());
            }else if (venderType == PusherVenderType.OPPO) {
                if(data == null || data.getAppSecret() == null || data.getAppPkgName() == null) {
                    LOGGER.warn("Pushing name= " + name + " data json error");
                    return;
                }
                LOGGER.debug("=========【PusherOppo】========= data={}",data);
                pusher = new PusherOppo(data.getAppSecret(), data.getAppPkgName(), data.getAppKey(), data.getMasterSecret());
            } else if (venderType == PusherVenderType.MEIZU) {
                if(data == null || data.getAppSecret() == null || data.getAppPkgName() == null) {
                    LOGGER.warn("Pushing name= " + name + " data json error");
                    return;
                }
                LOGGER.debug("=========【PusherMEIZU】========= data={}",data);
                pusher = new PusherMeizu(data.getAppId(), data.getAppSecret(), data.getAppPkgName());
            } else if (venderType == PusherVenderType.GETUI) {
                if(data == null || data.getAppSecret() == null || data.getAppPkgName() == null) {
                    LOGGER.warn("Pushing name= " + name + " data json error");
                    return;
                }
                LOGGER.debug("=========【PusherGetui】========= data={}",data);
                pusher = new PusherGetui(data.getAppId(), data.getAppSecret(), data.getAppPkgName(), data.getAppKey(), data.getMasterSecret());
            }

            LOGGER.debug("====pusherMap={}",pusherMap);

            PusherVender alreadyPut = pusherMap.putIfAbsent(name, pusher);
            if(alreadyPut != null) {
                //use the already put one
                pusher = alreadyPut;
            }
        }

        String[] ss = destLogin.getPusherIdentify().split(":");
        pusher.sendPushMessage(ss[ss.length - 1], msg, devMessage, senderLogin, destLogin);
    }

    @Override
    public void stopService(String name) {
        pusherMap.remove(name);
    }
}
