INSERT INTO `eh_search_types` (`id`, `namespace_id`, `name`, `content_type`, `status`, `create_time`) VALUES (1, 0, '快讯', 'news', '1', UTC_TIMESTAMP());
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('news.url', '/park-news/index.html/#/news/share/', 'news page url', 0, NULL);


-- merge from sa4.0 by xiongying
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 1, 'zh_CN', '提交申请通知给管理员', '您收到一条“${categoryName}”的申请 \n 预订人：“${creatorName}” \n 手机号：“${creatorMobile}” \n 公司名称：“${creatorOrganization}” \n 备注：“${note}” \n 您可以登录管理后台查看详情');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 2, 'zh_CN', '提交申请通知给机构', '您收到一条“${categoryName}”的申请 \n 预订人：“${creatorName}” \n 手机号：“${creatorMobile}” \n 公司名称：“${creatorOrganization} \n 备注：“${note}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 3, 'zh_CN', '邮件主题', '您收到一条“${categoryName}”的申请');
    
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`) 
    VALUES ('1', 'ServiceAlliance', 'BP融资模板', '提交BP', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"林龙","requiredFlag":"0"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"13163366563","requiredFlag":"0"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"左邻","requiredFlag":"0"},{"fieldName":"cityName","fieldDisplayName":"企业城市","fieldType":"string","fieldContentType":"text","fieldDesc":"企业所在城市","requiredFlag":"0"},{"fieldName":"industry","fieldDisplayName":"企业行业","fieldType":"string","fieldContentType":"text","fieldDesc":"企业所属行业","requiredFlag":"0"},{"fieldName":"financingStage","fieldDisplayName":"融资阶段","fieldType":"string","fieldContentType":"text","fieldDesc":"融资阶段","requiredFlag":"0"},{"fieldName":"financingAmount","fieldDisplayName":"融资金额","fieldType":"decimal","fieldContentType":"text","fieldDesc":"融资金额（万元）","requiredFlag":"0"},{"fieldName":"transferShares","fieldDisplayName":"出让股份","fieldType":"number","fieldContentType":"text","fieldDesc":"出让股份 %","requiredFlag":"0"},{"fieldName":"projectDesc","fieldDisplayName":"项目描述","fieldType":"string","fieldContentType":"text","fieldDesc":"项目描述","requiredFlag":"0"},{"fieldName":"attachments","fieldDisplayName":"附件图片","fieldType":"blob","fieldContentType":"image","fieldDesc":"","requiredFlag":"0"},{"fieldName":"attachments","fieldDisplayName":"商业计划书","fieldType":"blob","fieldContentType":"file","fieldDesc":"","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());


-- 插入审批类型对应的汉字文本，add by tt, 20160901
SET @max_id = (SELECT max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '1', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '2', 'zh_CN', '异常');

-- 审批错误提示，add by tt, 20160923
set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10000', 'zh_CN', '请选择请假类型');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10001', 'zh_CN', '请输入请假理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10002', 'zh_CN', '请选择请假时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10003', 'zh_CN', '请假开始时间必须小于请假结束时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10004', 'zh_CN', '所选时间包含已请假时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10005', 'zh_CN', '请假时间是非工作时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10006', 'zh_CN', '请假时长为0');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10007', 'zh_CN', '请输入申请理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10008', 'zh_CN', '类型名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10009', 'zh_CN', '类型名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10010', 'zh_CN', '该审批人设置关联了审批规则，请先删除对应的审批规则，再删除该审批人设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10011', 'zh_CN', '审批名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10012', 'zh_CN', '审批名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10013', 'zh_CN', '审批名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10014', 'zh_CN', '审批规则名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10015', 'zh_CN', '审批规则名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10016', 'zh_CN', '审批规则名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10017', 'zh_CN', '请假审批不能选择无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10018', 'zh_CN', '忘打卡审批不能为无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10019', 'zh_CN', '请输入驳回理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10020', 'zh_CN', '请选择需要审批的申请单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10021', 'zh_CN', '类型名称不能超过8个字');

-- 审批发送消息模板，add by tt, 20160923
set @id := (select max(id) from `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 11, 'zh_CN', '用户提交请假申请', '${creatorName}提交了请假申请，请假时间：${time}，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 12, 'zh_CN', '请假申请到下一级别', '${creatorName}提交了请假申请，请假时间：${time}，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 13, 'zh_CN', '请假申请通过', '您提交的请假申请已通过审批，请假时间：${time}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 14, 'zh_CN', '请假申请驳回', '您提交的请假申请被${approver}驳回，理由是：${reason}，请假时间：${time}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 21, 'zh_CN', '用户提交异常申请', '${creatorName}提交了异常申请，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 22, 'zh_CN', '异常申请到下一级别', '${creatorName}提交了异常申请，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 23, 'zh_CN', '异常申请通过', '您对${punchDate}提交的异常申请已通过审批。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 24, 'zh_CN', '异常申请驳回', '您对${punchDate}提交的异常申请被${approver}驳回，理由是：${reason}。', 0);


-- 在业主类型表中添加数据      2016/09/02 by xq.tian
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('1', '0', 'owner', '业主', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('2', '0', 'tenement', '租户', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('3', '0', 'relative', '亲属', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('4', '0', 'friend', '朋友', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('5', '0', 'nurse', '保姆', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('6', '0', 'agency', '地产中介', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('7', '0', 'readyAuth', '待审核', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('8', '0', 'other', '其他', '1');

-- 插入模板       2016/09/02 by xq.tian
SET @eh_locale_strings_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.behavior', 'immigration', 'zh_CN', '迁入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.behavior', 'emigration', 'zh_CN', '迁出');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '2', 'zh_CN', '无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '0', 'zh_CN', '未认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '1', 'zh_CN', '认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.livingStatus', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.livingStatus', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.primaryFlag', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.primaryFlag', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.parkingType', '2', 'zh_CN', '月卡');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.parkingType', '1', 'zh_CN', '临时');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14001', 'zh_CN', '业主已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14002', 'zh_CN', '业主不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14003', 'zh_CN', '客户手机号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15001', 'zh_CN', '导入失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16001', 'zh_CN', '车辆已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16002', 'zh_CN', '车辆不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16003', 'zh_CN', '车牌号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16004', 'zh_CN', '用户已经在车辆的使用者列表中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '17001', 'zh_CN', '用户已经在楼栋门牌中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '1', 'zh_CN', '男');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '2', 'zh_CN', '女');


-- 插入深圳湾发邮件的邮箱 add by xiongying20160924
set @configuration_id := (select max(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES (@configuration_id:=@configuration_id+1, 'mail.smtp.account', 'shenzhenbay@zuolin.com', '', '999987', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (@configuration_id:=@configuration_id+1, 'mail.smtp.passwod', 'Zuolin1802', '', '999987', NULL);	
    
-- 微信公众号的开发者id和秘钥 add by xiongying20160926
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (@configuration_id:=@configuration_id+1, 'wechat.server', 'https://api.weixin.qq.com', '', '0', NULL);	
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (@configuration_id:=@configuration_id+1, 'wechat.appKey', 'wxda4ca555d76459c1', '', '0', NULL);	
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (@configuration_id:=@configuration_id+1, 'wechat.appSecret', '73c5725ca71eeb22843a03f66b6425fb', '', '0', NULL);	