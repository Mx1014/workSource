// @formatter:off
package com.everhomes.pusher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.jooq.tools.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.apnshttp2.builder.ApnsClient;
import com.everhomes.apnshttp2.builder.impl.ApnsClientBuilder;
import com.everhomes.apnshttp2.notifiction.Notification;
import com.everhomes.apnshttp2.notifiction.Notification.Builder;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bundleid_mapper.BundleidMapper;
import com.everhomes.bundleid_mapper.BundleidMapperProvider;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.developer_account_info.DeveloperAccountInfo;
import com.everhomes.developer_account_info.DeveloperAccountInfoProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.messaging.ApnsServiceFactory;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.PushMessageResolver;
import com.everhomes.messaging.PusherService;
import com.everhomes.messaging.PusherVenderType;
import com.everhomes.messaging.PusherVendorService;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.message.MessageRecordStatus;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.rest.messaging.DeviceMessages;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.pusher.PushMessageCommand;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.rest.pusher.ThirdPartPushMessageCommand;
import com.everhomes.rest.pusher.ThirdPartResponseMessageDTO;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserLoginStatus;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.MessagePersistWorker;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.NetworkIOException;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

@Component
public class PusherServiceImpl implements PusherService, ApnsServiceFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(PusherServiceImpl.class);
	private final static String MESSAGE_INDEX_ID = "indexId";
	private final static String TYPE_PRODUCTION = "production";
	private final static String TYPE_DEVELOP = "develop";
	// private final static String TYPE_DEVELOP = "develop";
	/**
	 * add by huanglm 20180611,默认bundleId "com.ios" ,
	 * 凡是取不到开发者信息的时候就取这个bundleId对应的开发都信息
	 */
	private final static String DEFAULT_BUNDLEID = "com.ios";

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

	// @Autowired
	// WorkerPoolFactory workerPoolFactory;
	//
	// @Autowired
	// JesqueClientFactory jesqueClientFactory;

	@Autowired
	private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;

	@Autowired
	private BorderProvider borderProvider;

	@Autowired
	BigCollectionProvider bigCollectionProvider;

	@Autowired
	PusherVendorService pusherVendorService;

	@Autowired
	UserProvider userProvider;

	@Autowired
	UserService userService;
	
    @Autowired
    AppNamespaceMappingProvider appNamespaceMappingProvider;

	/**
	 * add by huanglm for IOS pusher update
	 */
	@Autowired
	DeveloperAccountInfoProvider developerAccountInfoProvider;

	/**
	 * add by huanglm for IOS pusher update
	 */
	@Autowired
	BundleidMapperProvider bundleidMapperProvider;
	
	@Autowired
	PusherService pusherService;
	
	@Autowired
	MessagingService messagingService;
	
	final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

	// private String queueName = "iOS-pusher2";

	ApnsService service = null;

	String messageBoxPrefix = "PUSH_MSG:";

	@Value("${apns.certname}")
	String certName = "apns-develop";

	private String namespacePrefix = "namespace:";

	ConcurrentMap<String, ApnsService> certMaps = new ConcurrentHashMap<String, ApnsService>();

	/**
	 * add by huanglm 20180604 用于存储http2与apns的连接
	 */
	ConcurrentMap<String, ApnsClient> http2ClientMaps = new ConcurrentHashMap<String, ApnsClient>();

	// 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
	// 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题
	// 由于本方法没有内容，故并没有使用ApplicationListener，若后面在setup上增加逻辑请同时增加ApplicationListener
	// by lqs 20180516
	// @PostConstruct
	public void setup() {
		// workerPoolFactory.getWorkerPool().addQueue(queueName);
	}

	/**
	 * 创建一个全新的，让旧的自动回收掉
	 */
	@Override
	public void flushHttp2ClientMaps() {
		this.http2ClientMaps = new ConcurrentHashMap<String, ApnsClient>();
	}

	@Override
	public ApnsService getApnsService(String partner) {
		ApnsService service = this.certMaps.get(partner);
		if (service == null) {
			// init hear
			Cert cert = certProvider.findCertByName(partner);
			if (cert != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(cert.getData());
				// .withCert("/home/janson/projects/pys/apns/apns_develop.p12",
				// "123456")
				ApnsServiceBuilder builder = APNS.newService().withCert(bis, cert.getCertPass().trim()).asPool(5);
				if (partner.indexOf("develop") >= 0) {
					builder = builder.withSandboxDestination();
				} else {
					builder = builder.withProductionDestination();
				}
				ApnsService innerService = builder.build();
				if (innerService == null) {
					LOGGER.warn("start apns server error");
					return null;
				}
				service = new PriorityQueuedApnsService(innerService, Executors.defaultThreadFactory());
				service.start();

				ApnsService tmp = this.certMaps.putIfAbsent(partner, service);
				if (tmp != null) {
					try {
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
		if (partner.startsWith(this.namespacePrefix)) {
			server = this.certMaps.remove(partner);
		} else if (partner.equals(this.certName)) {
			server = this.certMaps.remove(this.certName);
		}

		LOGGER.info("restarting apns service=" + partner);

		if (server != null) {
			server.stop();
		}

		pusherVendorService.stopService(partner);
	}

	private void pushMessageAndroid(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg,
			String platform, DeviceMessage devMessage) {
		Message message = new Message();
		message.setAppId(AppConstants.APPID_DEFAULT);
		// message.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
		message.setNamespaceId(destLogin.getNamespaceId());
		message.setChannelType("");
		message.setChannelToken("");
		message.setSenderUid(senderLogin.getUserId());

		Gson gson = new Gson();
		message.setContent(gson.toJson(devMessage));
		String key = getPushMessageKey(destLogin.getNamespaceId(), destLogin.getDeviceIdentifier());
		this.messageBoxProvider.putMessage(key, message);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Pushing message(push android), pushMsgKey=" + key + ", msgId=" + msgId + ", senderLogin="
					+ senderLogin + ", destLogin=" + destLogin);
		}

		PusherNotifyPdu pdu = new PusherNotifyPdu();
		pdu.setPlatform(platform);
		pdu.setNotification(msg.getContent());
		pdu.setMessageType("UNICAST");
		pdu.setMessageId(msgId);
		pdu.setDeviceId(destLogin.getDeviceIdentifier());

		long requestId = LocalSequenceGenerator.getNextSequence();
		borderConnectionProvider.broadcastToAllBorders(requestId, pdu);

		LOGGER.info("Pushing to uid=" + destLogin.getUserId());
	}

	/**
	 * 该苹果推送方法为IOS推送升级前的方法，是基于证书和socket的推送方式
	 * 
	 * @param senderLogin
	 * @param destLogin
	 * @param msgId
	 * @param msg
	 * @param platform
	 * @param devMessage
	 */
	private void pushMessageApple(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg, String platform,
			DeviceMessage devMessage) {
		PayloadBuilder payloadBuilder = APNS.newPayload();
		if (devMessage.getAlert().length() > 20) {
			payloadBuilder = payloadBuilder.alertBody(devMessage.getAlert().substring(0, 20));
		} else {
			payloadBuilder = payloadBuilder.alertBody(devMessage.getAlert());
		}

		payloadBuilder = payloadBuilder
				// .alertAction(devMessage.getAction())
				// .actionKey("testAction")
				// .category("testCategory")
				.alertTitle(devMessage.getTitle()).badge(devMessage.getBadge())
				// .forNewsstand() aps {content-available: 1}
				// .instantDeliveryOrSilentNotification()
				.customField("alertType", devMessage.getAlertType()).customField("appId", devMessage.getAppId());

		if (devMessage.getAudio() != null && !devMessage.getAudio().isEmpty()) {
			payloadBuilder = payloadBuilder.sound(devMessage.getAudio());
		}
		// if(devMessage.getAlertType().equals(DeviceMessageType.Jump.getCode()))
		// {
		// payloadBuilder = payloadBuilder.customField("jumpObj",
		// devMessage.getExtra().get("jumpObj"))
		// .customField("jumpType", devMessage.getExtra().get("jumpType"));
		// }
		if (null != devMessage.getAction()) {
			payloadBuilder = payloadBuilder.customField("actionType", devMessage.getAction()).customField("actionData",
					devMessage.getExtra().get("actionData"));
		}

		String payload = payloadBuilder.build();

		// .customField("", devMessage.)
		// .customField("", devMessage.)
		// String payload =
		// APNS.newPayload().alertBody(devMessage.getAlert()).build();
		// String identify =
		// "0e45353318a46f03269fbce18f6643475043d01d298ec4ef305a70b7c1de09ff";
		// String identify =
		// "b135d649736eedd8dbf649a245a42856d400d13fbf96ecc0a2746fb670f09471";
		String identify = destLogin.getDeviceIdentifier();
		identify = identify.replace("<", "").replace(">", "").replace(" ", "");
		String partner = certName;
		if (destLogin.getNamespaceId() > 0) {
			partner = this.namespacePrefix + destLogin.getNamespaceId();
		}
		if (destLogin.getPusherIdentify() != null) {
			partner = partner + ":" + destLogin.getPusherIdentify();
		}

		// String payload = APNS.newPayload().badge(3)
		// .customField("secret", "what do you think?")
		// .localizedKey("GAME_PLAY_REQUEST_FORMAT")
		// .localizedArguments("Jenna", "Frank")
		// .actionKey("Play").build();

		// if(msgId != 0) {
		// final Job job = new Job(PusherAction.class.getName(),
		// new Object[]{ payload, identify, partner });
		// jesqueClientFactory.getClientPool().enqueue(queueName, job);
		// } else {

		// use queue to notify

		int now = (int) (new Date().getTime() / 1000);
		boolean error = false;

		try {
			PriorityApnsNotification notification = new PriorityApnsNotification(
					EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
					now + 60 * 60 /* Expire in one hour */,
					identify /* Device Token */, payload, devMessage.getPriorigy());
			ApnsService tempService = getApnsService(partner);
			if (tempService != null) {
				tempService.push(notification);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Pushing message(push ios), pushMsgKey=" + partner + ", msgId=" + msgId + ", identify="
							+ identify + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin + "payload="
							+ payload);
				}
			} else {
				LOGGER.warn("Pushing apnsServer not found");
			}
		} catch (NetworkIOException e) {
			error = true;
			LOGGER.warn("apns error and stop it", e);
		} catch (Exception ex) {
			error = true;
			LOGGER.warn("apns error deviceId not correct", ex);
		}

		if (error) {
			try {
				stopApnsServiceByName(partner);
			} catch (Exception ex) {
				LOGGER.warn("stop apns service error", ex);
			}

		}

	}

	/**
	 * 该苹果推送方法为IOS推送升级的方法，是基于Token和http2的推送方式
	 * 
	 * @param senderLogin
	 * @param destLogin
	 * @param msgId
	 * @param msg
	 * @param platform
	 * @param devMessage
	 */
	private void pushMessageApplehttp2(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg,
			String platform, DeviceMessage devMessage) {
		// 1.组装推送信息；
		Builder notifBuilder = new Notification.Builder(destLogin.getDeviceIdentifier());
		notifBuilder = notifBuilder.alertBody(devMessage.getAlert());
		notifBuilder = notifBuilder.alertTitle(devMessage.getTitle()).badge(devMessage.getBadge())
				.customField("alertType", devMessage.getAlertType()).customField("appId", devMessage.getAppId());

		if (devMessage.getAudio() != null && !devMessage.getAudio().isEmpty()) {
			notifBuilder = notifBuilder.sound(devMessage.getAudio());
		}
		if (null != devMessage.getAction()) {
			notifBuilder = notifBuilder.customField("actionType", devMessage.getAction()).customField("actionData",
					devMessage.getExtra().get("actionData"));
		}

		// 2.获取http2与APNs连接好的客户端
		Notification notif = notifBuilder.build();
		String identify = destLogin.getDeviceIdentifier();

		// 获取域空间ID，该值无论如何都得有值
		Integer namespaceId = destLogin.getNamespaceId();
		namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
		try {
			ApnsClient client;
			client = getApnsClient(destLogin);
			if (client != null) {
				// 3.消息推送(回应消息暂无地方存放)
				client.addPush(notif);
				LOGGER.info(client.toString());
				// 无队列模式，方便定位问题调试，提交的时候记得注掉（与上一语句互斥）
				// NotificationResponse result = client.push(notif);
				if (LOGGER.isDebugEnabled()) {
					// LOGGER.warn("NotificationResponse:"+result);
					LOGGER.debug("Pushing message(push ios), namespaceId=" + namespaceId + ", msgId=" + msgId
							+ ", identify=" + identify + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin
							+ "payload=" + notif.getPayload());
				}
			} else {
				LOGGER.warn("Pushing apnsServer not found");
			}
		} catch (NetworkIOException e) {
			LOGGER.warn("apns error ", e);
		} catch (Exception ex) {
			LOGGER.warn("apns error deviceId not correct", ex);
		}
	}

	/**
	 * 
	 * @param destLogin
	 * @return iOS Xiaomi Huawei Android
	 */
	private String getPlatform(UserLogin destLogin) {
		if (destLogin.getStatus() == UserLoginStatus.LOGGED_OFF) {
			LOGGER.error("Pushing message, destLogin loggedoff, destLogin=" + destLogin);
			return null;
		}

		if (destLogin.getDeviceIdentifier() == null || destLogin.getDeviceIdentifier().isEmpty()) {
			return null;
		}

		if (destLogin.getPusherIdentify() != null) {
			if (destLogin.getPusherIdentify().startsWith("xiaomi:")) {
				return "xiaomi";
			} else if (destLogin.getPusherIdentify().startsWith("huawei:")) {
				return "huawei";
			}
		}

		Device d = this.deviceProvider.findDeviceByDeviceId(destLogin.getDeviceIdentifier());
		String platform = null;
		if (d == null) {
			LOGGER.warn("Pushing message, dest device not found, using auto detect, destLogin=" + destLogin);
			// auto detect by destLogin.getDeviceIdentifier()
			if (destLogin.getDeviceIdentifier().indexOf(":") >= 0) {
				platform = "android";
			} else if (destLogin.getDeviceIdentifier().length() >= 60) {
				platform = "iOS";
			}

			return platform;
		}

		platform = d.getPlatform();
		if (platform == null || !(platform.equals("iOS") || platform.equals("android"))) {
			// platform != iOS && platform != "android", auto detect by deviceId
			if (d.getDeviceId() != null) {
				if (d.getDeviceId().indexOf(":") >= 0) {
					platform = "android";
				} else if (d.getDeviceId().length() >= 60) {
					platform = "iOS";
				}
			}
		}

		return platform;
	}

	// https://developer.apple.com/library/
	// ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/ApplePushService.html
	// #//apple_ref/doc/uid/TP40008194-CH100-SW1
	public void pushMessage(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg) {
		if (null == destLogin.getDeviceIdentifier()) {
			LOGGER.error("Pushing message, destLogin deviceId is null, msgId=" + msgId + ", senderLogin=" + senderLogin
					+ ", destLogin=" + destLogin);
			return;
		}

		String beanName = PushMessageResolver.PUSH_MESSAGE_RESOLVER_PREFIX + msg.getAppId();
		PushMessageResolver messageResolver = PlatformContext.getComponent(beanName);
		if (null == messageResolver) {
			messageResolver = PlatformContext.getComponent(PushMessageResolver.PUSH_MESSAGE_RESOLVER_DEFAULT);
		}
		// assert(messageResolver != null)
		DeviceMessage devMessage = messageResolver.resolvMessage(senderLogin, destLogin, msg);

		String platform = getPlatform(destLogin);
		if (platform == null) {
			return;
		}

		if (platform.equals("iOS")) {
			// chagne by huanglm for IOS pusher update
			Integer namespaceId = destLogin.getNamespaceId();
			namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
			String flag = configProvider.getValue(namespaceId, ConfigConstants.APPLE_PUSHER_FLAG, "");
			// 苹果推送方式开关，值为1时为基于http2的新方式推送，其他值或空为旧方式推送
			if (flag.equals("1")) {
				LOGGER.info("configuration flag is {},there will use http2 new pusher ", flag);
				pushMessageApplehttp2(senderLogin, destLogin, msgId, msg, platform, devMessage);
			} else {
				LOGGER.info("configuration flag is {},there will use old pusher ", flag);
				pushMessageApple(senderLogin, destLogin, msgId, msg, platform, devMessage);
			}

		} else if (platform.equals("xiaomi")) {
			pusherVendorService.pushMessageAsync(PusherVenderType.XIAOMI, senderLogin, destLogin, msg, devMessage);
		} else if (platform.equals("huawei")) {
			pusherVendorService.pushMessageAsync(PusherVenderType.HUAWEI, senderLogin, destLogin, msg, devMessage);
		} else {
			// Android or other here
			pushMessageAndroid(senderLogin, destLogin, msgId, msg, platform, devMessage);
		}
	}

	private String getPushMessageKey(Integer namespaceId, String deviceId) {
		if (namespaceId == null || namespaceId.equals(0)) {
			return messageBoxPrefix + ":" + deviceId;
		} else {
			return messageBoxPrefix + ":" + namespaceId + ":" + deviceId;
		}
	}

	@Override
	public void createCert(Cert cert) {
		Cert tmp = this.certProvider.findCertByName(cert.getName());
		if (tmp != null) {
			this.certProvider.deleteCert(tmp);
		}

		this.certProvider.createCert(cert);

		this.stopApnsServiceByName(cert.getName());
	}

	@Override
	public DeviceMessages getRecentMessages(RecentMessageCommand cmd) {
		// UserLogin userLogin = UserContext.current().getLogin();

		int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getCount());
		String key = getPushMessageKey(cmd.getNamespaceId(), cmd.getDeviceId());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Pushing message(fetch), pushMsgKey=" + key + ", cmd=" + cmd + ", namespaceId="
					+ cmd.getNamespaceId());
		}

		MessageLocator l = new MessageLocator(key);
		l.setAnchor(cmd.getAnchor());

		List<Message> msgInBox = messageBoxProvider.getPastToRecentMessages(l, count, true);
		DeviceMessages deviceMsgs = new DeviceMessages();
		deviceMsgs.setAnchor(l.getAnchor());

		for (Message mb : msgInBox) {
			String msgStr = null;
			try {
				msgStr = mb.getContent();
				if (msgStr != null && !msgStr.isEmpty()) {
					DeviceMessage msg = (DeviceMessage) StringHelper.fromJsonString(msgStr, DeviceMessage.class);
					if (msg != null) {
						deviceMsgs.add(msg);
						Map actionData = (Map) StringHelper.fromJsonString(msg.getExtra().get("actionData"), Map.class);
						MessageRecordDto record = new MessageRecordDto();
						record.setAppId(mb.getAppId());
						record.setNamespaceId(mb.getNamespaceId());
						record.setMessageSeq(mb.getMessageSequence());

						record.setSenderUid(
								Long.valueOf(Double.valueOf(actionData.get("senderUid").toString()).intValue()));
						record.setSenderTag(MessageRecordSenderTag.FETCH_NOTIFY_MESSAGES.getCode());
						record.setDstChannelType(actionData.get("dstChannel").toString());
						record.setDstChannelToken(
								Double.valueOf(actionData.get("dstChannelId").toString()).intValue() + "");
						record.setBodyType(mb.getContextType());
						record.setBody(mb.getContent());
						record.setStatus(MessageRecordStatus.CORE_FETCH.getCode());
						record.setMeta(mb.getMeta());
						record.setCreateTime(new Timestamp(System.currentTimeMillis()));

						Map data_map = (Map) StringHelper.fromJsonString(mb.getContent(), Map.class);
						if (data_map.get("extra") != null) {
							Map extraData = (Map) data_map.get("extra");
							record.setIndexId(extraData.get(MESSAGE_INDEX_ID) != null
									? Long.valueOf(extraData.get(MESSAGE_INDEX_ID).toString()) : 0);
						}
						MessagePersistWorker.getQueue().offer(record);
					}
				}

			} catch (Exception ex) {
				LOGGER.error("device message error msgStr=" + msgStr, ex);
			}

		}

		return deviceMsgs;
	}

	@Override
	public void pushServiceTest(PushMessageCommand cmd) {
		ApnsService service = APNS.newService().withCert("/tmp/apns_appstore.p12", "zuolin").withAppleDestination(false)
				.build();

		// ApnsService service = getApnsService("namespace:1000000");

		String payload = APNS.newPayload().alertBody(cmd.getMessage() + Math.random()).build();
		String token = cmd.getDeviceId();
		service.push(token, payload);

		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String deviceToken : inactiveDevices.keySet()) {
			Date inactiveAsOf = inactiveDevices.get(deviceToken);
			LOGGER.info("date=" + inactiveAsOf + " deviceToken=" + deviceToken);
		}

		// http://www.concretepage.com/java/jdk-8/java-8-completablefuture-example
		// List<Integer> list = Arrays.asList(10,20,30,40);
		//
		// list.stream().map(data->CompletableFuture.supplyAsync(()->getNumber(data))).
		// map(compFuture->compFuture.thenApply(n->n*n)).map(t->t.join())
		// .forEach(s->System.out.println(s));

		// List<String> list = Arrays.asList("A","B","C","D");
		// list.stream().map(s->CompletableFuture.supplyAsync(()->s+s))
		// .map(f->f.whenComplete((result,error)->System.out.println(result+"
		// Error:"+error))).count();

		// Map<String, Long> deviceMap = new HashMap<String, Long>();
		// deviceMap.put("7e978fbb0d127671e30b4704414c7bdf272b06d066fccde8a2f309bcfa110393",
		// 0l);
		// deviceMap.put("frompython_195870_xiaoxiao2", 0l);
		// deviceMap = requestDevices(deviceMap);
		//
		// LOGGER.info("all device is: " + deviceMap);
	}

	// private static int getNumber(int a){
	// return a*a;
	// }

	@Override
	public Map<String, Long> requestDevices(Map<String, Long> deviceMap) {
		List<String> devs = new ArrayList<>();
		deviceMap.forEach((dev, t) -> devs.add(dev));

		List<Border> borders = this.borderProvider.listAllBorders();
		if (borders != null) {
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
				} catch (Exception e) {
					LOGGER.warn("get request device error " + e.getMessage());
					return null;
				}

			}).forEach((pdu) -> {

				if (pdu != null) {
					for (int i = 0; i < pdu.getDevices().size(); i++) {
						Long t = pdu.getLastValids().get(i);
						if (!t.equals(0l)) {
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

		if (o == null && platform != null && platform.equals("iOS")) {
			// iOS only
			pushMessage(senderLogin, destLogin, msgId, msg);
		}
	}

	// xiaomi test
	@Override
	public void sendXiaomiMessage() {
		// Constants.useOfficial();
		Sender sender = new Sender("3ijRnJlb08iLMfh6hyMvqw==");
		String messagePayload = "This is a message by zuolin";
		String title = "notification title zuolin";
		String description = "notification description zuolin";
		com.xiaomi.xmpush.server.Message message = new com.xiaomi.xmpush.server.Message.Builder().title(title)
				.description(description).payload(messagePayload)
				.restrictedPackageName("com.everhomes.android.oa.debug").passThrough(0).notifyType(1) // 使用默认提示音提示
				.build();
		String regId = "mCSNgs9e5UWI6En0EAI9guxt4Qje6UcqLo295M3DORs=";
		Result result;
		try {
			result = sender.send(message, regId, 3);
			LOGGER.info("Server response: ", "MessageId: " + result.getMessageId() + " ErrorCode: "
					+ result.getErrorCode().toString() + " Reason: " + result.getReason());
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * add by huanglm 建立与APNs的http2连接
	 * 
	 * @return
	 */
	public ApnsClient getApnsClient(UserLogin destLogin) {
		String identify = destLogin.getDeviceIdentifier();
		if (identify == null) {
			LOGGER.warn("identify is null  and Unable to establish a connection");
			return null;
		}
		// bundleId 一般从Device中取，但为了兼容旧数据，Device中取不到的话可以namespaceId +
		// pusherIdentify
		String bundleId = null;
		// 推送服务器类型默认为生产
		boolean isProductionGateway = true;
		// 获取设备注册时保存的bundleId
		Device device = this.deviceProvider.findDeviceByDeviceId(identify);
		if (device == null) {
			LOGGER.warn("device is null  and Unable to establish a connection");
			return null;
		}
		bundleId = device.getBundleId();
		String pusherServiceType = device.getPusherServiceType();
		// 设置为开发服务器类型
		if (TYPE_DEVELOP.equals(pusherServiceType)) {
			isProductionGateway = false;
		}
		// bundleId 为空的情况一般是旧应用的推送，为了兼容需要从映射表中取得bundleId（新应用bundleId一般不为空）
		if (StringUtils.isBlank(bundleId)) {
			String pidentyfy = destLogin.getPusherIdentify();
			if (StringUtils.isBlank(pidentyfy)) {
				LOGGER.warn("bundleId is null  and pusherIdentify is null , it will use DEFAULT_BUNDLEID");
				// return null;
			} else {
				// 获取域空间ID，该值无论如何都得有值
				Integer namespaceId = destLogin.getNamespaceId();
				namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
				BundleidMapper bmapper = bundleidMapperProvider.findBundleidMapperByParams(namespaceId, pidentyfy);
				if (bmapper == null || StringUtils.isBlank(bmapper.getBundleId())) {
					LOGGER.warn("namespaceId = " + namespaceId + ";identify = " + pidentyfy
							+ "; can not find data or bundleId from mapper table ,it will use DEFAULT_BUNDLEID");
					// return null;
				} else {
					bundleId = bmapper.getBundleId();
					if (bmapper.getIdentify() != null && TYPE_DEVELOP.equals(bmapper.getIdentify())) {// 判断服务器类型
						isProductionGateway = false;
					}
				}
			}
		}
		if (bundleId == null) {
			bundleId = DEFAULT_BUNDLEID;
			LOGGER.warn("bundleId is null ");
		}
		// 优先从http2ClientMaps 中 取，如没有再创建新的连接
		ApnsClient client = this.http2ClientMaps.get(bundleId);
		if (client != null) {
			LOGGER.info("use map client .client{}", client.toString());
			return client;
		}

		// 获取开发者信息
		DeveloperAccountInfo dlaInfo = developerAccountInfoProvider.getDeveloperAccountInfoByBundleId(bundleId);
		// 取不到开发者账号，则取默认的开发者账号
		if (dlaInfo == null) {
			LOGGER.warn(
					"bundleId is " + bundleId + " ; can not find DeveloperAccountInfo ,it will use DEFAULT_BUNDLEID");
			dlaInfo = developerAccountInfoProvider.getDeveloperAccountInfoByBundleId(DEFAULT_BUNDLEID);
		}
		if (dlaInfo == null) {
			// 查询不到开发者信息，则不往下走了，因为没法建立与APNs服务的连接
			LOGGER.warn("can not find DeveloperAccountInfo by DEFAULT_BUNDLEID  and Unable to establish a connection");
			return null;
		}
		byte[] authkey = dlaInfo.getAuthkey();
		String teamId = dlaInfo.getTeamId();
		String authKeyId = dlaInfo.getAuthkeyId();
		String authkeyStr = new String(authkey);

		try {
			client = new ApnsClientBuilder().inSynchronousMode().withProductionGateway(isProductionGateway)
					.withApnsAuthKey(authkeyStr.trim()).withTeamID(teamId.trim()).withKeyID(authKeyId.trim())
					.withDefaultTopic(bundleId.trim()).build();
			// if(LOGGER.isDebugEnabled()) {
			// LOGGER.warn("NotificationResponse:"+result);
			LOGGER.info("Pushing message(build client), bundleId=" + bundleId + ", isProductionGateway="
					+ isProductionGateway + ", authkeyStr=" + authkeyStr + ", teamId=" + teamId + ", authKeyId="
					+ authKeyId);
			// }
		} catch (NetworkIOException e) {
			LOGGER.warn("apns error and stop it", e);
		} catch (Exception ex) {
			LOGGER.warn("apns error deviceId not correct", ex);
		}
		// 将 client
		// 转为PriorityQueuedApnsClient，让client推送PriorityQueuedApnsClient中队列的消息，也让添加到client的消息存在队列中
		// 测试调试时可以把下两句注掉让其消息直接发送不经过队列，方便调试
		client = new PriorityQueuedApnsClient(client, Executors.defaultThreadFactory());
		client.start();

		ApnsClient tmp = this.http2ClientMaps.putIfAbsent(bundleId, client);
		if (tmp != null) {
			try {
				client.stop();
			} catch (Exception e) {
				LOGGER.warn("stop client server error");
			}
			client = tmp;
		}

		return client;
	}

	/**
	 * 停止bundleId对应的连接端，add by huanglm
	 */
	public void stophttp2Client(String bundleId) {
		ApnsClient client = null;
		client = this.http2ClientMaps.remove(bundleId);
		if (client != null) {
			client.shutdown();
		}

	}

	/**
	 * 第三方调用发送消息，add by moubinmo 20181026
	 */
	@Override
	public ThirdPartResponseMessageDTO thirdPartPushMessage(ThirdPartPushMessageCommand cmd) {
		LOGGER.debug("【ThirdPartPushMessageCommand】=",cmd);
		// 1.用户token列表/发送失败手机号列表/这里设置默认发送方式为3（同时推送和应用消息）
		String[] tokenArray = cmd.getIdentifierTokenList().split(",");
		List<String> unReachTokenList = new ArrayList<String>(tokenArray.length);		 
		Integer MSG_DEFAULT_SEND_TYPE= 3; 	
		
		// 2.响应内容
		ThirdPartResponseMessageDTO response = new ThirdPartResponseMessageDTO();
		
		for(String token : tokenArray){
			
			// 3.1 验证接受者在左邻系统是否存在,由 appkey/appsecret确定namespace
			AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
			
			// 3.2(tocken/namespace确定一个用户)
			UserIdentifier user = userProvider.findClaimedIdentifierByTokenAndNamespaceId(token,appNamespaceMapping.getNamespaceId());
			LOGGER.debug("appNamespaceMapping : "+ appNamespaceMapping == null?"appNamespaceMapping is null":appNamespaceMapping.toString());
			
			if (user != null) {
				// 4.1 消息构造
				MessageDTO messageDto = new MessageDTO();
				messageDto.setAppId(AppConstants.APPID_MESSAGING);
				messageDto.setSenderUid(User.SYSTEM_UID);
				messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), user.getOwnerUid().toString()));
				messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
				messageDto.setCreateTime(System.currentTimeMillis());
				messageDto.setBodyType(MessageBodyType.TEXT.getCode());
				if(cmd.getMsgType() == 1){//上车信息
					messageDto.setBody(thirdPartMessageBuild("班车消息通知：线路名称为【 ",cmd.getRouteName()," 】即将到达 【 ",cmd.getNextStation()," 】站，请您做好乘车准备！"));				
				}else if(cmd.getMsgType() == 2){//下车信息
					messageDto.setBody(thirdPartMessageBuild("班车消息通知：线路名称为【 ",cmd.getRouteName()," 】即将到达 【 ",cmd.getNextStation()," 】站，请您做好下车准备！"));				
				}else{
					// 4.2 班车信息类型有误则返回标志位3
					messageDto.setBody("班车信息类型有误，请稍后重试！");
					response.setCode(3);
					response.setMsg("FAIL");
					response.setExtra("");
					return response;
				}
				
				// 4.3 根据 msgType 推送方式推送给用户
				messagingService.routeMessage(null, User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING,ChannelType.USER.getCode(), String.valueOf(user.getOwnerUid()), messageDto, MSG_DEFAULT_SEND_TYPE);
//				LOGGER.debug("调用第三方信息推送接口：=====【发送成功】===== message= ",messageDto.toString());

			}else{
				// 4.4 用户不存在，则加入失败列表
				unReachTokenList.add(token);
//				LOGGER.debug("调用第三方信息推送接口：=====【用户不存在】===== token=",token);
				continue;
			}
		}
		
		if(unReachTokenList.size() > 0){
			// 4.5 有发送失败号码则返回标志位2和失败号码列表
			response.setCode(2);
			response.setMsg("FAIL");
			StringBuffer sbuffer = new StringBuffer();
			for(String i : unReachTokenList){
				sbuffer.append(i).append(",");
			}
			response.setExtra(sbuffer.toString());
			return response;
		}
		
		// 4.6 成功则返回标志位1
		response.setCode(1);
		response.setMsg("SUCCESS");
		response.setExtra("");
		return response;
	}
	
	/**
	 * 用于第三方消息字符串的组装
	 * */
	private String thirdPartMessageBuild(String... list){
		StringBuffer stringBuffer = new StringBuffer();
		for(String string : list){
			stringBuffer.append(string);
		}
		return stringBuffer.toString();
	}
	
}
