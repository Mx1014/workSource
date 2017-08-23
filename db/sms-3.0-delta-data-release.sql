--
-- 联信通  短信配置 add by xq.tian  2017/08/30
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.lxt.server', '', '联信通server', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.lxt.spId', '', '联信通账户', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.lxt.authCode', '', '联信通密码', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.lxt.srcId', '', '联信通srcId', 0, NULL);

--
-- 优讯通
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.yxt.server', '', '优讯通server', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.yxt.accountName', '', '优讯通账户', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.yxt.password', '', '优讯通密码', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@configurations_id := @configurations_id + 1), 'sms.yxt.token', '', '优讯通token', 0, NULL);

--
-- 短信签名
--
SET @max_template_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '﻿左邻短信签名', '【﻿左邻】', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '科技园短信签名', '【科技园】', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '讯美短信签名', '【讯美】', 999999);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '金隅嘉业短信签名', '【金隅嘉业】', 999995);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '海岸物业短信签名', '【海岸物业】', 999993);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '深业物业短信签名', '【深业物业】', 999992);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '威新LINK短信签名', '【威新LINK】', 999991);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '储能短信签名', '【储能】', 999990);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '金地Ibase短信签名', '【金地Ibase】', 999989);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '爱特家短信签名', '【爱特家】', 999988);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '深圳湾短信签名', '【深圳湾】', 999987);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '创源短信签名', '【创源】', 999986);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '华润置地OE短信签名', '【华润置地OE】', 999985);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '清华信息港短信签名', '【清华信息港】', 999984);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', 'T空间短信签名', '【T空间】', 999982);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '正中会短信签名', '【正中会】', 999983);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '星商汇园区短信签名', '【星商汇园区】', 999981);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '全至100短信签名', '【全至100】', 999980);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '光大we谷短信签名', '【光大we谷】', 999979);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '康利K生活短信签名', '【康利K生活】', 999978);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', 'Volgo短信签名', '【Volgo】', 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '创梦云短信签名', '【创梦云】', 999977);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '名网邦短信签名', '【名网邦】', 999976);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '荣超股份短信签名', '【荣超股份】', 999975);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '嘉定新城智慧管家短信签名', '【嘉定新城智慧管家】', 999974);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '保集e智谷短信签名', '【保集e智谷】', 999973);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '军民融合APP短信签名', '【军民融合APP】', 999972);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '张江高科短信签名', '【张江高科】', 999971);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '互联网产业园短信签名', '【互联网产业园】', 999970);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '昌发展短信签名', '【昌发展】', 999969);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '国贸圈短信签名', '【国贸圈】', 999968);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '大沙河建投短信签名', '【大沙河建投】', 999967);

--
-- 短信模板
--
SET @max_template_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 1, 'zh_CN', '验证码', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 4, 'zh_CN', '派单', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 6, 'zh_CN', '任务2', '${operatorUName}给你分配了一个任务，请直接联系用户${createUName}（电话${createUToken}），帮他处理该问题。', 0);
--
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 3, 'zh_CN', '物业缴费通知', '您${year}年${month}月物业账单为，本月金额:${dueAmount},往期欠款:${oweAmount}，本月实付金额:${payAmount}，应付金额:${balance}， 请尽快使用左邻缴纳物业费。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 7, 'zh_CN', '新报修', '${createUName}(手机号：${createUToken})已发布了新的报修任务，主题为：${subject}，请立即处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 8, 'zh_CN', '门禁', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id} （24小时有效）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 8, 'zh_CN', '门禁', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：http://core.zuolin.com/aclink/v?id=${id} （24小时有效）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 9, 'zh_CN', '看楼申请', '用户${userName}（手机号：${userPhone}）于${applyTime}提交了预约${applyType}申请：参观位置：${location}\n面积需求：${area}\n公司名称：${enterpriseName}\n备注：${description}', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 10, 'zh_CN', '物业2', '${operatorName} ${operatorPhone}已将一个${categoryName}单派发给你，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 11, 'zh_CN', '物业', '${operatorName} ${operatorPhone}已发起一个${categoryName}单，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 12, 'zh_CN', '预定1', '您已成功预约了${resourceName}，使用时间：${useDetail}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 13, 'zh_CN', '预定2', '您已成功预约了${resourceName}，使用时间：${useDetail}，预约数量：${count}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 14, 'zh_CN', '预定3', '您已成功预约了${resourceName}，使用详情：${useDetail}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 15, 'zh_CN', '物业任务3', '您于${day}日${hour}时发起的服务已派发至处理人 ${operatorName}（电话： ${operatorPhone}），请耐心等待。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 17, 'zh_CN', '合两有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 18, 'zh_CN', '合两无', '尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 19, 'zh_CN', '合一有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 20, 'zh_CN', '合一无', '尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 21, 'zh_CN', '发新有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，欢迎入住${communityName}，有任何问题请随时与我联系。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 22, 'zh_CN', '发新无', '尊敬的客户您好，欢迎入住${communityName}。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 23, 'zh_CN', '排队', '您有一个关于${parkingLotName}停车场的月卡申请任务，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 24, 'zh_CN', '排队驳回', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）审核未通过，请前往查看详情。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 25, 'zh_CN', '	待办理', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）已审核通过，请前往查看详情（逾期未办理将取消资格）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 26, 'zh_CN', '待办办理', '恭喜，您的停车月卡（停车场：${parkingLotName}，车牌：${plateNumber}）已成功办理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 27, 'zh_CN', '待办取消', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）审核未通过，请前往查看详情。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 28, 'zh_CN', '资申成', '您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 29, 'zh_CN', '资申败', '您申请预约的${useTime}的${resourceName}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 30, 'zh_CN', '资付成', '您已完成支付，成功预约${useTime}的${resourceName}，请按照预约的时段使用资源，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 31, 'zh_CN', '资申成', '您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 32, 'zh_CN', '资超时', '您申请预约的${useTime}的${resourceName}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 33, 'zh_CN', '资催办', '客户（${userName}${userPhone}）提交资源预约的线下支付申请，预约${resourceName}，使用时间：${useTime}，订单金额${price}，请尽快联系客户完成支付。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 34, 'zh_CN', '物业任务4', '（${creatorName}，电话：${creatorPhone}）已发起物业报修，由（处理人：${operatorName}、电话：${operatorPhone}）负责处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 35, 'zh_CN', '入驻待处', '您有客户（姓名：${applyUserName}，电话：${applyContact}）园区入驻流程待处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 36, 'zh_CN', '驻待处受', '您的园区入驻需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 37, 'zh_CN', '驻处理驳', '您的园区入驻任务未通过，具体原因见APP中“我的申请”。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 38, 'zh_CN', '待办办理', '（姓名：${applyUserName}、电话${applyContact}）督办您处理他的园区入驻申请 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 39, 'zh_CN', '驻成功', '您的园区入驻需求已办理完毕，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 40, 'zh_CN', '资待处理', '（姓名：${userName}、电话${userPhone}）督办您处理他的园区入驻申请 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 41, 'zh_CN', '资待督办', '请督办（处理人:${operatorName}，处理人电话:${operatorContact}）处理（姓名：${userName}，电话：${userPhone}）的${resourceName}预约申请任务。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 42, 'zh_CN', '资待受理', '您的${resourceName}预约需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 43, 'zh_CN', '资待驳回', '您的${resourceName}预约任务未通过，具体原因见APP内“我的申请”。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 44, 'zh_CN', '资待催办', '（姓名：${userName}，电话：${userPhone}）催办您处理他的${resourceName}预约申请。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 45, 'zh_CN', '资办成功', '您的${resourceName}预约需求已办理完毕，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 46, 'zh_CN', '报修受督', '请督办（处理人:${operatorName}，处理人电话:${operatorContact}）处理（姓名：${userName}，电话：${userPhone}）的${resourceName}预约申请任务。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 47, 'zh_CN', '报修待分', '您的${resourceName}预约需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 48, 'zh_CN', '报待分督', '客户（${operatorName}，电话：${operatorPhone}）的一个新的${categoryName}任务超时未分配人员，请督办尽快受理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 49, 'zh_CN', '报修完成', '您的${categoryName}任务已处理完毕，可在APP内“我-我的申请”中查看完成情况说明，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 50, 'zh_CN', '正中会', '客户${userName}（联系方式：${userPhone}）完成支付，成功预约${useTime}的${resourceName}，请提前做好相关准备工作。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 51, 'zh_CN', '视频会', '视频会议账号${accountName}将在一周后（${date}）到期，请及时处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 52, 'zh_CN', '视测会', '视频会议试用账号${accountName}将在3天后（${date}）到期,请及时处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 53, 'zh_CN', '申诉', '您的申诉已通过，账号手机已更新为${newIdentifier}，若非本人操作请联系客服，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 54, 'zh_CN', '物业催缴', '${targetName}先生/女士，您好，您的物业账单已出，账期${dateStr}，使用"${appName} APP"可及时查看账单并支持在线付款。', 0);
