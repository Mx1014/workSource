
DELETE FROM `eh_acl_privileges` WHERE `id` > 199;

DELETE FROM `eh_web_menus`;

DELETE FROM `eh_web_menu_privileges`;

-- 权限数据
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (200,0,'发帖','发公告和任务贴',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (201,0,'推送消息','推送消息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (301,0,'分配人员','分配任务',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (302,0,'修改任务可见性','可将任务修改为可见或不可见',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (303,0,'查询报修任务帖子','查看报修任务贴','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (304,0,'查询求助任务帖子','查询求助任务帖子','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (305,0,'查询所有任务帖子','查看所有任务帖子','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (306,0,'接受／拒绝任务','接受／拒绝任务',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (307,0,'处理任务状态','处理任务状态',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (308,0,'设置任务的分类','设置任务的分类',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (309,0,'信息统计','任务统计',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (310,0,'官方活动','官方活动',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (380,0,'查看区信息','查看区信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (381,0,'修改区信息','修改区信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (390,0,'楼栋列表','楼栋列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (391,0,'楼栋的增删改','楼栋的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (401,0,'查询园区企业','查询园区企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (402,0,'增加企业','增加企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (403,0,'删除企业','删除企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (404,0,'修改企业','修改企业信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (405,0,'开通企业管理员账号','开通企业管理员账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (406,0,'查看用户列表','查看用户列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (407,0,'用户的统计','用户的统计',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (408,0,'门牌管理','门牌的管理',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (409,0,'用户认证','用户认证',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (410,0,'认证审批','同意/拒绝',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (503,0,'查看物业缴费统计','查看物业缴费统计',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (504,0,'查询物业缴费单','查询物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (505,0,'查询物业缴费记录','查询物业缴费记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (506,0,'查询欠费家庭','查询欠费家庭',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (507,0,'给所有欠费家庭推送缴费通知','给所有欠费家庭推送缴费通知',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (508,0,'给指定家庭发缴费通知','给指定家庭发缴费通知',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (509,0,'上传缴费账单','上传缴费账单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (510,0,'删除物业缴费单','删除物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (511,0,'添加物业缴费单','添加物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (512,0,'增加停车充值项','增加停车充值项',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (513,0,'删除停车充值项','删除停车充值项',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (514,0,'设置月卡有效领取天数','设置月卡有效领取天数',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (515,0,'查询充值记录','查询充值记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (516,0,'查询月卡申请记录','查询月卡申请记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (517,0,'发放月卡','发放月卡',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (518,0,'领取月卡','领取月卡',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (519,0,'设置通用设置','设置通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (520,0,'查询通用设置','查询通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (521,0,'查询服务类型','查询服务类型',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (522,0,'服务类型的增删改','增删改服务预约类型权限，包括服务类型的启用停用状态修改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (523,0,'查询预约详情','查询预约详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (524,0,'设置订单完成状态','设置订单完成状态',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (525,0,'查询数据统计','查询数据统计',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (526,0,'设置场所通用设置','设置场所预订通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (527,0,'查询场所','查询场所',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (528,0,'添加/修改具体场所','添加/修改具体场所',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (529,0,'查询预订详情','查询预订详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (530,0,'添加具体场所预订规则','添加具体场所预订规则',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (531,0,'删除场所规则','删除场所规则',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (532,0,'查询具体场所商品信息','查询具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (533,0,'添加具体场所商品信息','添加具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (534,0,'删除具体场所商品信息','删除具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (535,0,'删除具体场所','删除具体场所',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (536,0,'查看招租信息','查看招租信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (537,0,'查看入驻申请','查看入驻申请',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (538,0,'删除入驻申请','删除入驻申请',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (539,0,'增删改招租信息','增删改招租信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (540,0,'查看服务联盟信息','查看服务联盟信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (541,0,'增删改服务联盟信息','增删改服务联盟信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (542,0,'查看创客空间信息','查看创客空间信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (543,0,'删除修改创客空间','删除修改创客空间',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (544,0,'考勤设置','考勤设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (545,0,'查看打卡详情','查看打卡详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (546,0,'查看异常统计','查看异常统计',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (547,0,'进行异常处理','进行异常处理',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (548,0,'查看账号','查看账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (549,0,'分配账号','分配账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (550,0,'更换用户','更换用户',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (551,0,'查看我的订单','查看我的订单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (552,0,'查看账号信息','查看账号信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (553,0,'查看开票信息','查看开票信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (554,0,'查看服务热线','查看服务热线',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (555,0,'增加删除服务热线','增加删除服务热线',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (556,0,'停车设置查询','停车设置查询',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (557,0,'设置活动规则','停车设置查询',null);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (600,0,'查看子公司列表信息','查看子公司列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (601,0,'增删改分公司','增删改分公司',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (602,0,'查看角色权限列表','查看角色权限列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (603,0,'增删改角色，设置权限','增删改角色，设置权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (604,0,'查看管理员信息','查看管理员信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (605,0,'管理员的增删改','管理员的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (606,0,'查看部门','查看部门',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (607,0,'部门的增删改','部门的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (608,0,'查看通讯录','查看通讯录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (609,0,'通讯录人员的增删改','通讯录人员的增删改',null);



INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (610,0,'设置角色','设置角色',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (611,0,'查看业务组','查看业务组',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (612,0,'业务组增删改','业务组增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (613,0,'业务组成员设置','业务组成员设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (614,0,'组成员的修改和删除','组成员的修改和删除',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (615,0,'组成员权限设置','组成员权限设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (616,0,'管辖范围','管辖范围，包括查询管辖小区楼栋，添加删除小区楼栋',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (620,0,'审批列表','审批的通讯录列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (621,0,'审批认证','同意，拒绝审批',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (700,0,'参考标准','参考标准 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (701,0,'类型管理','类型管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (702,0,'任务列表','任务列表 全部功能',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (710,0,'任务审阅','任务审阅 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (711,0,'权重管理','权重管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (712,0,'绩效统计','绩效统计 全部功能',null);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (720,0,'门禁管理','门禁管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (721,0,'用户授权','用户授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (722,0,'访客授权','访客授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (723,0,'版本管理 ','版本管理  全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (724,0,'门禁分组','门禁分组 全部功能',null);




-- 菜单模块数据
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (10000,'消息管理',0,'fa fa-volume-up',null,1,2,'/10000','park',100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11000,'论坛/公告',10000,null,'forum_notice',0,2,'/10000/11000','park',110);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (12000,'一键推送',10000,null,'messagepush',0,2,'/10000/12000','park',120);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (20000,'任务管理',0,'fa fa-coffee',null,1,2,'/20000','park',200);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (21000,'全部任务',20000,null,'all_task',0,2,'/20000/21000','park',210);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (22000,'我的任务',20000,null,'my_task',0,2,'/20000/22000','park',220);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (23000,'统计',20000,null,'statistics',0,2,'/20000/23000','park',230);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (30000,'项目管理',0,'fa fa-building',null,1,2,'/30000','park',300);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (30500,'区管理',30000,null,'community_list',0,2,'/30000/30500','park',305);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (31000,'楼栋管理',30000,null,'building_management',0,2,'/30000/31000','park',310);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (32000,'门牌管理',30000,null,'apartment_statistics',0,2,'/30000/32000','park',320);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (33000,'企业管理',30000,null,'enterprise_management',0,2,'/30000/33000','park',330);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (34000,'用户管理',30000,null,'user_management',0,2,'/30000/34000','park',340);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (35000,'用户认证',30000,null,'user_identify',0,2,'/30000/35000','park',350);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (40000,'运营服务',0,'fa fa-comment',null,1,2,'/40000','park',400);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41000,'停车充值',40000,null,null,1,2,'/40000/41000','park',410);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41100,'停车设置',41000,null,'park_setting',0,2,'/40000/41000/41100','park',411);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41200,'活动规则',41000,null,'park_rules',0,2,'/40000/41000/41200','park',412);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41300,'月卡申请',41000,null,'park_card',0,2,'/40000/41000/41300','park',413);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41400,'充值记录',41000,null,'park_recharge',0,2,'/40000/41000/41400','park',414);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42000,'场所预订',40000,null,null,1,2,'/40000/42000','park',420);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42100,'通用设置',42000,null,'rental_setting',0,2,'/40000/42000/42100','park',421);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42200,'场所发布',42000,null,'rental_publish',0,2,'/40000/42000/42200','park',422);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42300,'预订详情',42000,null,'rental_detail',0,2,'/40000/42000/42300','park',423);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43000,'招租管理',40000,null,null,1,2,'/40000/43000','park',430);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43100,'招租管理',43000,null,'rent_manage',0,2,'/40000/43000/43100','park',431);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43200,'入住申请',43000,null,'enter_apply',0,2,'/40000/43000/43200','park',432);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44000,'服务联盟',40000,null,'service_alliance',0,2,'/40000/44000','park',455);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (45000,'创客空间',40000,null,'market_zone',0,2,'/40000/45000','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (46000,'服务热线',40000,null,'service_hotline',0,2,'/40000/46000','park',457);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (50000,'内部管理',0,'fa fa-group',null,1,2,'/50000','park',500);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'层级管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'层级管理',51000,null,null,0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'人员管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员',52000,null,null,0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'部门通讯录',52000,null,null,0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'群组通讯录',52000,null,null,0,2,'/50000/52000/52300','park',523);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52400,'认证管理',52000,null,null,0,2,'/50000/52000/52400','park',524);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (53000,'管辖范围',50000,null,null,1,2,'/50000/53000','park',530);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (53100,'管辖范围',53000,null,null,0,2,'/50000/53000/53100','park',531);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (54000,'权限管理',50000,null,null,1,2,'/50000/54000','park',540);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (54100,'权限管理',54000,null,null,0,2,'/50000/54000/54100','park',541);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55000,'物业管理',50000,null,null,1,2,'/50000/55000','park',550);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55100,'设备巡检',55000,null,null,0,2,'/50000/55000/55100','park',551);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55200,'品质核查',55000,null,null,0,2,'/50000/55000/55200','park',552);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56000,'OA管理',50000,null,null,1,2,'/50000/56000','park',560);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56100,'考勤管理',56000,null,null,1,2,'/50000/56000/56100','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'考勤设置',56100,null,null,0,2,'/50000/56000/56100/56110','park',563);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'打卡详情',56100,null,'attendance_record',0,2,'/50000/56000/56100/56120','park',564);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'异常统计',56100,null,'attendance_result',0,2,'/50000/56000/56100/56130','park',565);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'异常处理',56100,null,'attendance_manage',0,2,'/50000/56000/56100/56140','park',566);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56200,'视频会议',56000,null,null,1,2,'/50000/56000/56200','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56210,'账号管理',56200,null,null,0,2,'/50000/56000/56200/56210','park',568);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56220,'我的订单',56200,null,null,0,2,'/50000/56000/56200/56220','park',569);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58000,'物业服务',50000,null,null,1,2,'/50000/58000','park',600);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58100,'品质核查',58000,null,null,1,2,'/50000/58000/58100','park',610);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58110,'作业标准',58100,null,null,1,2,'/50000/58000/58100/58110','park',611);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58111,'参考标准',58110,null,'reference_standard',0,2,'/50000/58000/58100/58110/58111','park',612);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58112,'类型管理',58110,null,'type_management',0,2,'/50000/58000/58100/58110/58112','park',613);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58113,'任务列表',58110,null,'task_list',0,2,'/50000/58000/58100/58110/58113','park',614);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58120,'品质分析',58100,null,null,1,2,'/50000/58000/58100/58120','park',614);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58121,'任务审阅',58120,null,'task_review',0,2,'/50000/58000/58100/58120/58121','park',615);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58122,'权重管理',58120,null,'weighting_management',0,2,'/50000/58000/58100/58120/58122','park',616);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58123,'绩效统计',58120,null,'performance_statistics',0,2,'/50000/58000/58100/58120/58123','park',617);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47000,'公共门禁',40000,null,null,1,2,'/40000/47000','park',432);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47100,'门禁管理',47000,null,'access_manage',0,2,'/40000/47000/47100','park',433);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47150,'版本管理',47000,null,'version_manage',0,2,'/40000/47000/47150','park',434);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47160,'门禁分组',47000,null,'access_group',0,2,'/40000/47000/47160','park',435);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47200,'用户授权',47000,null,'user_auth',0,2,'/40000/47000/47200','park',436);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (47300,'访客授权',47000,null,'visitor_auth',0,2,'/40000/47000/47300','park',437);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (48000,'服务预约',40000,null,null,1,2,'/40000/48000','park',440);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (48100,'通用设置',48000,null,'service_setting',0,2,'/40000/48000/48100','park',441);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (48200,'服务类型',48000,null,'service_type',0,2,'/40000/48000/48200','park',442);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (48300,'预约详情',48000,null,'service_detail',0,2,'/40000/48000/48300','park',443);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (48400,'数据统计',48000,null,'service_statistics',0,2,'/40000/48000/48400','park',444);


-- 模块权限数据
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (1,200,11000,'发帖',1,1,'发公告和任务贴',10);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (2,201,12000,'推送消息',1,1,'推送消息',20);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (3,306,21000,'接受，拒绝任务',0,1,'接受，拒绝任务',30);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (4,307,21000,'任务处理',0,1,'修改任务状态',40);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (5,303,21000,'查询报修任务帖子',1,1,'查询报修任务帖子',50);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (6,304,21000,'查看求助任务帖子',1,1,'查看求助任务帖子',60);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (7,305,21000,'查看所有任务帖子',1,1,'查看所有任务帖子',70);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (8,306,22000,'接受，拒绝任务',0,1,'接受，拒绝任务',79);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (77,307,22000,'任务处理',0,1,'修改任务状态',80);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (9,303,22000,'查询报修任务帖子',1,1,'查询报修任务帖子',81);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (75,304,22000,'查看求助任务帖子',1,1,'查看求助任务帖子',82);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (74,305,22000,'查看所有任务帖子',1,1,'查看所有任务帖子',83);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (73,309,23000,'任务统计',1,1,'任务统计',88);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (76,390,31000,'楼栋列表',1,1,'楼栋列表',90);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (72,391,31000,'楼栋的增删改',0,1,'楼栋的增删改',91);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (10,408,32000,'门牌管理',1,1,'门牌管理操作',100);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (11,401,33000,'企业列表',1,1,'查询企业列表',110);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (12,402,33000,'增加企业',0,1,'增加企业',120);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (13,403,33000,'删除企业',0,1,'删除企业',130);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (14,404,33000,'修改企业',0,1,'修改企业',140);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (15,405,33000,'开通企业管理员',0,1,'开通企业管理员',150);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (16,406,34000,'用户列表',1,1,'查询用户列表',160);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (17,556,41100,'停车设置列表',1,1,'查询停车设置列表',170);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (18,512,41100,'增加停车充值项',0,1,'增加停车充值项',180);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (19,513,41100,'删除停车充值项',0,1,'删除停车充值项',190);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (20,557,41200,'设置活动规则',1,1,'设置活动规则',200);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (21,515,41400,'充值记录',1,1,'查询充值记录',210);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (22,516,41300,'月卡申请记录',1,1,'查询月卡申请记录',220);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (23,514,41300,'设置月卡有效领取天数',0,1,'设置月卡有效领取天数',230);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (24,517,41300,'发放月卡',0,1,'发放月卡',240);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (25,518,41300,'领取月卡',0,1,'领取月卡',250);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (26,526,42100,'设置通用设置',1,1,'设置通用设置',260);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (27,527,42200,'查询场所',1,1,'查询场所',270);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (28,528,42200,'添加/修改具体场所',0,1,'添加/修改具体场所',280);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (29,530,42200,'添加具体场所预订规则',0,1,'添加具体场所预订规则',290);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (30,531,42200,'删除场所规则',0,1,'删除场所规则',300);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (31,532,42200,'查询具体场所商品信息',0,1,'查询具体场所商品信息',310);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (32,533,42200,'添加具体场所商品信息',0,1,'添加具体场所商品信息',320);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (33,534,42200,'删除具体场所商品信息',0,1,'删除具体场所商品信息',330);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (34,535,42200,'删除具体场所',0,1,'删除具体场所',340);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (35,529,42300,'查询预订详情',1,1,'查询预订详情',260);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (36,536,43100,'招租信息列表',1,1,'查看招租信息列表',260);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (37,539,43100,'增删改招租信息',0,1,'增删改招租信息',340);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (38,537,43200,'查看入驻申请',1,1,'查看入驻申请',260);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (39,538,43200,'删除入驻申请',0,1,'删除入驻申请',340);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (40,554,46000,'查看服务热线',1,1,'查看服务热线信息',350);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (41,555,46000,'增加删除服务热线',0,1,'增加删除服务热线',360);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (42,540,44000,'查看服务联盟',1,1,'查看服务联盟信息',370);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (43,541,44000,'增删改服务联盟信息',0,1,'增删改服务联盟信息',380);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (44,542,45000,'查看创客空间',1,1,'查看创客空间信息',390);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (45,543,45000,'删除修改创客空间',0,1,'删除修改创客空间',400);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (46,600,51100,'子公司列表',1,1,'查看子公司列表信息',410);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (47,601,51100,'增删改分公司',0,1,'增删改分公司',420);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (48,604,52100,'查看管理员信息',1,1,'查看管理员信息',430);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (49,605,52100,'管理员的增删改',0,1,'管理员的增删改',440);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (50,606,52200,'查看部门',1,1,'查看部门',450);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (51,607,52200,'部门的增删改',0,1,'部门的增删改',460);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (52,608,52200,'查看通讯录',1,1,'查看通讯录',470);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (53,609,52200,'通讯录人员的增删改',0,1,'通讯录人员的增删改',480);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (54,609,52200,'设置角色',0,1,'设置角色',490);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (55,611,52300,'查看业务组',1,1,'查看业务组',500);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (56,612,52300,'业务组增删改',0,1,'业务组增删改',510);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (57,613,52300,'业务组成员设置',1,1,'业务组成员设置',520);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (58,614,52300,'组成员的修改和删除',0,1,'组成员的修改和删除',530);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (59,615,52300,'组成员权限设置',0,1,'组成员权限设置',540);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (60,602,54100,'查看角色权限列表',1,1,'查看角色权限列表',550);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (61,603,54100,'增删改角色，设置权限',0,1,'增删改角色，设置权限',560);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (62,544,56110,'考勤设置',1,1,'考勤设置',570);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (63,545,56120,'打卡详情',1,1,'打卡详情',580);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (64,546,56130,'异常统计',1,1,'异常统计',590);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (65,547,56140,'打卡详情',1,1,'打卡详情',600);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (66,548,56210,'查看账号',1,1,'查看账号',610);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (67,549,56210,'分配账号',0,1,'分配账号',620);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (68,550,56210,'更换用户',0,1,'更换用户',630);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (69,551,56220,'我的订单',1,1,'查看我的订单',640);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (70,552,56220,'查看账号信息',0,1,'查看账号信息',650);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (71,553,56220,'查看开票信息',0,1,'查看开票信息',660);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (87,616,53100,'管辖范围管理',1,1,'管辖范围管理，包括管辖的小区楼栋查询，添加，删除',545);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (81,700,58111,'参考标准',1,1,'参考标准 全部权限',700);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (82,701,58112,'类型管理',1,1,'类型管理 全部权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (83,702,58113,'任务列表',1,1,'任务列表 全部权限',720);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (84,710,58121,'任务审阅',1,1,'任务审阅 全部权限',730);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (85,711,58122,'权重管理',1,1,'权重管理 全部权限',740);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (86,712,58123,'绩效统计',1,1,'绩效统计 全部权限',750);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (89,620,52400,'审批列表',1,1,'绩效统计 全部权限',760);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (88,621,52400,'审批认证',0,1,'审批同意，拒绝',770);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (90,380,30500,'查看区信息',1,1,'查看区信息',88);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (91,381,30500,'修改区信息',0,1,'修改区信息',89);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (92,409,35000,'用户认证',1,1,'用户认证',165);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (93,410,35000,'认证审核',0,1,'拒绝，同意',166);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (94,720,47100,'门禁管理',1,1,'门禁管理',345);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (95,721,47200,'用户授权',1,1,'用户授权',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (96,722,47300,'访客授权',1,1,'访客授权',347);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (97,519,48100,'查询通用设置',1,1,'查询通用设置',348);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (98,520,48100,'通用设置',0,1,'通用设置',348);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (99,521,48200,'查询服务类型',1,1,'查询服务类型',349);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (100,522,48200,'服务类型的增删改',0,1,'服务类型的增删改',349);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (101,523,48300,'查询预约详情',1,1,'查询预约详情',350);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (102,525,48400,'查询数据统计',1,1,'数据统计',350);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (103,723,47150,'版本管理',1,1,'版本管理',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (104,724,47160,'门禁分组',1,1,'门禁分组',346);


-- SELECT MAX(id) FROM `eh_web_menu_privileges`;
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11100,'活动管理',10000,null,'forum_activity',0,2,'/10000/11100','park',115);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),200,11100,'活动管理',1,1,'查询活动贴以及发布活动',15);



-- 20160525
-- SELECT MAX(id) FROM `eh_web_menu_privileges`;
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (713,0,'缴费记录','缴费记录 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (714,0,'设置','设置 全部功能',null);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43500,'缴费管理',40000,null,null,1,2,'/40000/43500','park',435);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43501,'缴费记录',43500,null,'property_fee_record',0,2,'/40000/43500/43501','park',436);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43502,'设置',43500,null,'property_fee_config',0,2,'/40000/43500/43502','park',437);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),713,43501,'缴费记录',1,1,'缴费记录',341);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),714,43502,'设置',1,1,'设置',342);

-- 20160530
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (715,0,'Wifi热点','Wifi热点 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (46100,'Wifi热点',40000,null,'wifi_hotspot',0,2,'/40000/46100','park',458);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),715,46100,'Wifi热点',1,1,'Wifi热点',401);

-- 20160615
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (725,0,'内部 门禁管理','内部 门禁管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (726,0,'内部 用户授权','内部 用户授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (727,0,'内部 访客授权','内部 访客授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (728,0,'内部 版本管理 ','内部 版本管理  全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (729,0,'内部 门禁分组','内部 门禁分组 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59000,'内部门禁',50000,null,null,1,2,'/50000/59000','park',700);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59100,'门禁管理',59000,null,'access_manage_inside',0,2,'/50000/59000/59100','park',702);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59150,'版本管理',59000,null,'version_manage_inside',0,2,'/50000/59000/59150','park',704);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59160,'门禁分组',59000,null,'access_group_inside',0,2,'/50000/59000/59160','park',706);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59200,'用户授权',59000,null,'user_auth_inside',0,2,'/50000/59000/59200','park',708);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59300,'访客授权',59000,null,'visitor_auth_inside',0,2,'/50000/59000/59300','park',710);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),725,59100,'门禁管理',1,1,'门禁管理',780);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),726,59200,'用户授权',1,1,'用户授权',810);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),727,59300,'访客授权',1,1,'访客授权',820);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),728,59150,'版本管理',1,1,'版本管理',790);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),729,59160,'门禁分组',1,1,'门禁分组',800);



-- 20160618
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
DELETE FROM `eh_web_menus` WHERE `id` in (58000,58100,58110,58111,58112,58120,58121,58122,58130,58131,58132,58140);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58000,'物业服务',50000,null,null,1,2,'/50000/58000','park',600);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58100,'品质核查',58000,null,null,1,2,'/50000/58000/58100','park',610);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58110,'手动核查',58100,null,null,1,2,'/50000/58000/58100/58110','park',611);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58111,'行为规范',58110,null,'behavior_standard',0,2,'/50000/58000/58100/58110/58111','park',612);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58112,'绩效统计',58110,null,'performance_statistics',0,2,'/50000/58000/58100/58110/58112','park',613);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58120,'作业标准',58100,null,null,1,2,'/50000/58000/58100/58120','park',614);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58121,'参考标准',58120,null,'reference_standard',0,2,'/50000/58000/58100/58120/58121','park',615);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58122,'类型管理',58120,null,'type_management',0,2,'/50000/58000/58100/58120/58122','park',616);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58130,'任务列表',58100,null,null,1,2,'/50000/58000/58100/58130','park',617);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58131,'任务列表',58130,null,'task_list',0,2,'/50000/58000/58100/58130/58131','park',618);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58132,'任务审阅',58130,null,'task_review',0,2,'/50000/58000/58100/58130/58132','park',619);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58140,'修改记录',58100,null,'edit_record',0,2,'/50000/58000/58100/58140','park',620);


DELETE FROM `eh_acl_privileges` WHERE `id` in (700,701,702,710,711,712);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (751,0,'行为规范','行为规范 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (752,0,'绩效统计','绩效统计 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (753,0,'参考标准','参考标准 全部功能',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (754,0,'类型管理','类型管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (755,0,'任务列表','任务列表 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (756,0,'任务审阅','任务审阅 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (757,0,'修改记录','修改记录 全部功能',null);


DELETE FROM `eh_web_menu_privileges` WHERE `id` in (81,82,83,84,85,86);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),751,58111,'行为规范',1,1,'行为规范 全部权限',700);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),752,58112,'绩效统计',1,1,'绩效统计 全部权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),753,58121,'参考标准',1,1,'参考标准 全部权限',720);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),754,58122,'类型管理',1,1,'类型管理 全部权限',730);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),755,58131,'任务列表',1,1,'任务列表 全部权限',740);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),756,58132,'任务审阅',1,1,'任务审阅 全部权限',750);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),757,58140,'修改记录',1,1,'修改记录 全部权限',760);



-- 20160621
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (760,0,'开卡用户','开卡用户 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (761,0,'充值记录','充值记录 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (762,0,'消费记录','消费记录 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43600,'一卡通',40000,null,null,1,2,'/40000/43500','park',440);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43610,'开卡用户',43600,null,'card_user',0,2,'/40000/43600/43610','park',441);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43620,'充值记录',43600,null,'card_recharge_record',0,2,'/40000/43600/43620','park',442);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43630,'消费记录',43600,null,'card_purchase_record',0,2,'/40000/43600/43630','park',443);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),760,43610,'开卡用户',1,1,'开卡用户 全部权限',345);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),761,43620,'充值记录',1,1,'充值记录 全部权限',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),762,43630,'消费记录',1,1,'消费记录 全部权限',347);


-- 20160705
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (220,0,'新闻管理','新闻管理 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11200,'新闻管理',10000,null,'news_management',0,2,'/10000/11200','park',116);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),220,11200,'新闻管理',1,1,'新闻管理  全部权限',16);


-- 20160712
-- 20160730 by sfyan
-- 根据具体情况去掉菜单 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES(47, 43300,'', 'EhNamespaces', 0 , 0);
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (558,0,'工位空间管理','空间管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (559,0,'工位 预订详情','预订详情 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43300,'工位预订',40000,null,null,1,2,'/40000/43300','park',435);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43310,'空间管理',43300,null,'station_booking_rent_manage',0,2,'/40000/43300/43310','park',436);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43320,'预订详情',43300,null,'station_booking_enter_apply',0,2,'/40000/43300/43320','park',437);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),558,43310,'空间管理',1,1,'空间管理 全部权限',341);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),559,43320,'入住申请',1,1,'入住申请 全部权限',342);

-- 资源预订 by sfyan 20160727
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (565,0,'默认参数','默认参数 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (566,0,'资源发布','资源发布 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (567,0,'预订详情','预订详情 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (568,0,'退款处理','退款处理 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43400,'资源预订',40000,null,null,1,2,'/40000/43400','park',438);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43410,'默认参数',43400,null,'rental_setting',0,2,'/40000/43400/43410','park',439);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43420,'资源发布',43400,null,'rental_publish',0,2,'/40000/43400/43420','park',440);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43430,'预订详情',43400,null,'rental_info',0,2,'/40000/43400/43430','park',441);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43440,'退款处理',43400,null,'refund_management',0,2,'/40000/43400/43440','park',442);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),565,43410,'默认参数',1,1,'默认参数  全部权限',343);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),566,43420,'资源发布',1,1,'资源发布 全部权限',344);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),559,43320,'预订详情',1,1,'预订详情 全部权限',342);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),568,43440,'退款处理',1,1,'退款处理 全部权限',346);


set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),310,11100,'官方活动',0,1,'官方活动',16);


DELETE FROM `eh_web_menus` WHERE `id` IN (51000, 51100, 52000, 52100, 52200, 52300, 54000, 54100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'组织架构管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'组织架构管理',51000,null,'architecture_management',0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'角色管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员管理',52000,null,'admin_management',0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'角色与权限管理',52000,null,'roles_management',0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'成员管理',52000,null,'personal_management',0,2,'/50000/52000/52300','park',523);



DELETE FROM `eh_web_menus` WHERE `id` IN (51000, 51100, 52000, 52100, 52200, 52300, 54000, 54100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'组织架构管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'组织架构管理',51000,null,'architecture_management',0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'角色管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员管理',52000,null,'admin_management',0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'角色与权限管理',52000,null,'roles_management',0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'成员管理',52000,null,'personal_management',0,2,'/50000/52000/52300','park',523);

DELETE FROM `eh_acl_privileges` WHERE `id` >= 600 AND `id` <= 615;
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (600,0,'查看子公司列表信息','查看子公司列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (601,0,'增删改分公司','增删改分公司',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (604,0,'查看管理员信息','查看管理员信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (605,0,'管理员的增删改','管理员的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (606,0,'查看角色与权限','查看角色与权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (607,0,'角色与权限的增删改','角色与权限的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (608,0,'成员列表','成员列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (609,0,'成员的增删改','成员的增删改',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (610,0,'设置角色','设置角色',null);


DELETE FROM `eh_web_menu_privileges` WHERE `id` >= 46 AND `id` <= 61;

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (46,600,51100,'组织架构列表',1,1,'查看组织架构列表',410);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (47,601,51100,'增删改组织架构',0,1,'增删改组织架构',420);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (48,604,52100,'查看管理员信息',1,1,'查看管理员信息',430);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (49,605,52100,'管理员的增删改',0,1,'管理员的增删改',440);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (50,606,52200,'查看角色与权限',1,1,'角色与权限查看',450);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (51,607,52200,'角色与权限的增删改',0,1,'角色与权限的增删改',460);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (52,608,52300,'成员列表',1,1,'成员列表查看',470);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (53,609,52300,'成员的增删改',0,1,'成员的增删改',480);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (54,610,52300,'设置角色',0,1,'设置角色',490);

-- 结算管理 by sfyan 20160810
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49000,'结算管理',40000,null,'settlement_management',0,2,'/40000/49000','park',454);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (770,0,'结算管理','结算管理 全部功能',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),770,49000,'结算管理',1,1,'结算管理',349);


-- 园区报 by sfyan 20160811
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (780,0,'园区报管理','园区报管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (781,0,'约稿须知','约稿须知  全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49500,'园区报',40000,null,null,1,2,'/40000/43400','park',453);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49510,'园区报管理',49500,null,'park_epaper_management',0,2,'/40000/49500/49510','park',452);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49520,'约稿须知',49500,null,'manuscripts_notice',0,2,'/40000/49500/49520','park',453);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),780,49510,'园区报管理',1,1,'园区报管理  全部权限',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),781,49520,'约稿须知',1,1,'约稿须知 全部权限',347);


-- 考勤管理 by sfyan 20160811
DELETE FROM `eh_acl_privileges` WHERE `id` IN (544, 545, 546, 547);
DELETE FROM `eh_web_menu_privileges` WHERE `id` IN (62, 63, 64, 65);
DELETE FROM `eh_web_menus` WHERE `id` IN (56100, 56110, 56120, 56130, 56140);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56100,'考勤管理',56000,NULL,NULL,1,2,'/50000/56000/56100','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56105,'考勤规则',56100,NULL,NULL,1,2,'/50000/56000/56100/56105','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'规则管理',56105,NULL,'punch_rule',0,2,'/50000/56000/56100/56105/56110','park',563);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'时间管理',56105,NULL,'punch_time',0,2,'/50000/56000/56100/56105/56120','park',564);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'地点管理',56105,NULL,'punch_location',0,2,'/50000/56000/56100/56105/56130','park',565);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'wifi管理',56105,NULL,'punch_wifi',0,2,'/50000/56000/56100/56105/56140','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56150,'排班管理',56105,NULL,'punch_scheduling',0,2,'/50000/56000/56100/56105/56150','park',568);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56160,'通用设置',56105,NULL,'punch_setting',0,2,'/50000/56000/56100/56105/56160','park',569);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56170,'个人设置',56105,NULL,'punch_personal_setting',0,2,'/50000/56000/56100/56105/56170','park',570);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (790,0,'规则管理','规则管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (791,0,'时间管理','时间管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (792,0,'地点管理','地点管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (793,0,'wifi管理','wifi管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (794,0,'排班管理','排班管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (795,0,'通用设置','通用设置',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (796,0,'个人设置','个人设置',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),790,56110,'规则管理',1,1,'规则管理  全部权限',570);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),791,56120,'时间管理',1,1,'时间管理 全部权限',571);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),792,56130,'地点管理',1,1,'地点管理 全部权限',572);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),793,56140,'wifi管理',1,1,'wifi管理 全部权限',573);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),794,56150,'排班管理',1,1,'排班管理 全部权限',574);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),795,56160,'通用设置',1,1,'通用设置 全部权限',575);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),796,56170,'个人设置',1,1,'个人设置 全部权限',576);


-- 添加业主自动关联
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (36000,'业主管理',30000,null,'apartment_info',0,2,'/30000/36000','park',360);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (411,0,'业主管理','业主管理',null);
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),411,36000,'业主管理',1,1,'业主管理 全部权限',167);

-- 深业物业的设备巡检
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58200,'后台',58000,null,null,1,2,'/50000/58000/58200','park',800);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58210,'参考标准',58200,null,null,1,2,'/50000/58000/58200/58210','park',802);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58211,'标准列表',58210,null,'equipment_inspection_standard_list',0,2,'/50000/58000/58200/58210/58211','park',804);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58212,'设备关联审批',58210,null,'equipment_inspection_check_attachment',0,2,'/50000/58000/58200/58210/58212','park',806);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58220,'设备台帐',58200,null,null,1,2,'/50000/58000/58200/58220','park',820);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58221,'设备列表',58220,null,'equipment_inspection_equipment_list',0,2,'/50000/58000/58200/58220/58221','park',822);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58222,'备品备件',58220,null,'equipment_inspection_sparepart_list',0,2,'/50000/58000/58200/58220/58222','park',824);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58230,'任务列表',58200,null,null,1,2,'/50000/58000/58200/58230','park',830);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58231,'任务列表',58230,null,'equipment_inspection_task_list',0,2,'/50000/58000/58200/58230/58231','park',832);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (800,0,'标准列表','标准列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (801,0,'设备关联审批','设备关联审批',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (802,0,'设备列表','设备列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (803,0,'备品备件','备品备件',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (804,0,'设备 任务列表','任务列表',null);


set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),800,58211,'标准列表',1,1,'标准列表  全部权限',600);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),801,58212,'设备关联审批',1,1,'设备关联审批 全部权限',601);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),802,58221,'设备列表',1,1,'设备列表 全部权限',602);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),803,58222,'备品备件',1,1,'备品备件 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),804,58231,'任务列表',1,1,'任务列表 全部权限',604);

-- 更新服务联盟的菜单
-- 更新服务联盟的菜单
DELETE FROM `eh_web_menus` WHERE `id` = 44000;
DELETE FROM `eh_acl_privileges` WHERE `id` IN (540, 541);
DELETE FROM `eh_web_menu_privileges` WHERE `id` IN (40, 41);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44000,'服务联盟',40000,NULL,NULL,1,2,'/40000/44000','park',455);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44100,'类型管理',44000,NULL,'service_type_management',0,2,'/40000/44000/44100','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44200,'机构管理',44000,NULL,'service_alliance',0,2,'/40000/44000/44200','park',457);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (540,0,'服务联盟 类型管理','服务联盟 类型管理 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (541,0,'服务联盟 机构管理','服务联盟 机构管理 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),540,44100,'类型管理',1,1,'备品备件 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),541,44200,'机构管理',1,1,'任务列表 全部权限',604);

UPDATE `eh_web_menus` SET `name` = '设备巡检' where `id` = 58200;


-- 把打卡考勤的老功能重新开放  by sfyan 20160817  
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56172,'打卡详情',56105,null,'attendance_record',0,2,'/50000/56000/56100/56105/56172','park',571);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56174,'异常统计',56105,null,'attendance_result',0,2,'/50000/56000/56100/56105/56174','park',572);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56176,'异常处理',56105,null,'attendance_manage',0,2,'/50000/56000/56100/56105/56176','park',573);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (797,0,'打卡详情','打卡详情',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (798,0,'异常统计','异常统计',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (799,0,'异常处理','异常处理',NULL);

-- 增加考勤统计菜单 by sfyan 20160822
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56106,'考勤统计',56100,null,null,1,2,'/50000/56000/56100/56106','park',571);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56111,'考勤统计',56106,null,'punch_statistics',0,2,'/50000/56000/56100/56106/56111','park',572);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56112,'打卡详情',56106,null,'punch_detail',0,2,'/50000/56000/56100/56106/56112','park',573);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (820,0,'考勤统计','考勤统计',null);


set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),820,56111,'考勤统计',1,1,'考勤统计  全部权限',577);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56112,'打卡详情',1,1,'打卡详情 全部权限',578);


-- 增加广告管理菜单 by xq.tian 20160825
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49600,'广告管理',40000,null,null,0,2,'/40000/49600','park',458);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (830,0,'广告管理','广告管理',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),830,49600,'广告管理',1,1,'广告管理  全部权限',577);

-- 整理公司内部管理菜单 by sfyan 20160827
DELETE FROM `eh_web_menus` WHERE `id` IN (51000, 51100, 52000, 52100, 52200, 52300, 54000, 54100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'组织架构管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'组织架构管理',51000,null,'architecture_management',0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'角色管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员管理',52000,null,'admin_management',0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'角色与权限管理',52000,null,'roles_management',0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'成员管理',52000,null,'personal_management',0,2,'/50000/52000/52300','park',523);

DELETE FROM `eh_acl_privileges` WHERE `id` >= 600 AND `id` <= 615;
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (600,0,'查看子公司列表信息','查看子公司列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (601,0,'增删改分公司','增删改分公司',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (604,0,'查看管理员信息','查看管理员信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (605,0,'管理员的增删改','管理员的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (606,0,'查看角色与权限','查看角色与权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (607,0,'角色与权限的增删改','角色与权限的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (608,0,'成员列表','成员列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (609,0,'成员的增删改','成员的增删改',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (610,0,'设置角色','设置角色',null);


DELETE FROM `eh_web_menu_privileges` WHERE `id` >= 46 AND `id` <= 61;

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (46,600,51100,'组织架构列表',1,1,'查看组织架构列表',410);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (47,601,51100,'增删改组织架构',0,1,'增删改组织架构',420);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (48,604,52100,'查看管理员信息',1,1,'查看管理员信息',430);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (49,605,52100,'管理员的增删改',0,1,'管理员的增删改',440);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (50,606,52200,'查看角色与权限',1,1,'角色与权限查看',450);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (51,607,52200,'角色与权限的增删改',0,1,'角色与权限的增删改',460);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (52,608,52300,'成员列表',1,1,'成员列表查看',470);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (53,609,52300,'成员的增删改',0,1,'成员的增删改',480);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (54,610,52300,'设置角色',0,1,'设置角色',490);


-- 增加 门禁日志 和 官网会议菜单 by sfyan 20160912
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59400,'门禁日志',59000,null,'access_log_inside',0,2,'/50000/59000/59400','park',711);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56230,'会议官网',56200,null,'zlMeeting',0,2,'/50000/56000/56200/56230','park',570);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (730,0,'内部 门禁日志','内部 门禁日志 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (560,0,'会议官网','会议官网',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),730,59400,'门禁日志',1,1,'门禁日志',801);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),560,56230,'会议官网',1,1,'查看会议官网',641);



-- 考勤管理的菜单整理 by sfyan 20160922
-- 删除之前的考勤管理菜单数据
DELETE FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

DELETE FROM `eh_acl_privileges` WHERE `id` IN (790,791,792,793,794,795,796,797,798,799,820,821,822,823);

DELETE FROM `eh_web_menu_privileges` WHERE `menu_id` IN (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');

-- 考勤规则设置

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'考勤规则设置',56100,NULL,NULL,1,2,'/50000/56000/56100/56110','park',100);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56111,'规则管理',56110,NULL,'punch_rule',0,2,'/50000/56000/56100/56110/56111','park',110);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56112,'时间管理',56110,NULL,'punch_time',0,2,'/50000/56000/56100/56110/56112','park',120);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56113,'地点管理',56110,NULL,'punch_location',0,2,'/50000/56000/56100/56110/56113','park',130);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56114,'wifi管理',56110,NULL,'punch_wifi',0,2,'/50000/56000/56100/56110/56114','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56115,'排班管理',56110,NULL,'punch_scheduling',0,2,'/50000/56000/56100/56110/56115','park',140);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (790,0,'规则管理','规则管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (791,0,'时间管理','时间管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (792,0,'地点管理','地点管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (793,0,'wifi管理','wifi管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (794,0,'排班管理','排班管理',NULL);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),790,56111,'规则管理',1,1,'规则管理  全部权限',100);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),791,56112,'时间管理',1,1,'时间管理 全部权限',110);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),792,56113,'地点管理',1,1,'地点管理 全部权限',120);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),793,56114,'wifi管理',1,1,'wifi管理 全部权限',130);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),794,56115,'排班管理',1,1,'排班管理 全部权限',140);

-- 审批规则设置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'审批规则设置',56100,NULL,NULL,1,2,'/50000/56000/56100/56120','park',200);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56121,'审批规则',56120,NULL,'punch_rule',0,2,'/50000/56000/56100/56120/56121','park',210);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56122,'审批人设置',56120,NULL,'punch_time',0,2,'/50000/56000/56100/56120/56122','park',220);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (822,0,'审批规则','审批规则 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (823,0,'审批人设置','审批人设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),822,56121,'审批规则',1,1,'审批规则 全部权限',210);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),823,56122,'审批人设置',1,1,'审批人设置 全部权限',220);

-- 考勤规则配置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'考勤规则配置',56100,NULL,NULL,1,2,'/50000/56000/56100/56130','park',300);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56131,'通用设置',56130,NULL,'punch_setting',0,2,'/50000/56000/56100/56130/56131','park',310);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56132,'个人设置',56130,NULL,'punch_personal_setting',0,2,'/50000/56000/56100/56130/56132','park',320);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (795,0,'通用设置','通用设置 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (796,0,'个人设置','个人设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),795,56131,'通用设置',1,1,'通用设置 全部权限',310);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),796,56132,'个人设置',1,1,'个人设置 全部权限',320);

-- 打卡详情
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'打卡详情',56100,null,null,1,2,'/50000/56000/56100/56140','park',400);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56141,'打卡详情',56140,null,'punch_detail',0,2,'/50000/56000/56100/56140/56141','park',410);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (797,0,'打卡详情','打卡详情 全部权限',null);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56141,'打卡详情',1,1,'打卡详情 全部权限',410);


-- 异常处理
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56150,'异常处理',56100,null,null,1,2,'/50000/56000/56100/56150','park',500);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56151,'异常申请',56150,null,'abnormal_apply',0,2,'/50000/56000/56100/56150/56151','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56152,'请假申请',56150,null,'leave_apply',0,2,'/50000/56000/56100/56150/56152','park',520);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (798,0,'异常申请','异常申请 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (799,0,'请假申请','请假申请 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),798,56151,'异常申请',1,1,'异常申请 全部权限',510);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),799,56152,'请假申请',1,1,'请假申请 全部权限',520);


-- 考勤统计
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56160,'考勤统计',56100,null,null,1,2,'/50000/56000/56100/56160','park',600);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56161,'考勤统计',56160,null,'punch_statistics',0,2,'/50000/56000/56100/56160/56161','park',610);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (820,0,'考勤统计','考勤统计 全部权限',null);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),820,56161,'考勤统计',1,1,'考勤统计  全部权限',610);

-- 请假类型设置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56170,'请假类型设置',56100,null,null,1,2,'/50000/56000/56100/56170','park',700);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56171,'类型设置',56170,null,'leave_setting',0,2,'/50000/56000/56100/56170/56171','park',710);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (821,0,'请假类型设置','请假类型设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),821,56171,'类型设置',1,1,'类型设置 全部权限',710);

-- 物业保修2.0的菜单权限 by sunwen 20160830
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (24000,'任务列表',20000,NULL,'task_management_list',0,2,'/20000/24000','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (25000,'服务类型设置',20000,NULL,'service_type_setting',0,2,'/20000/25000','park',457);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (26000,'分类设置',20000,NULL,'classify_setting',0,2,'/20000/26000','park',458);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (27000,'统计',20000,NULL,'task_statistics',0,2,'/20000/27000','park',459);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (904,0,'查看所有任务','任务管理 查看所有任务',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (905,0,'任务管理 服务类型设置','任务管理 服务类型设置 全部权限',NULL);
  
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('331', '0', '分派任务', '分派任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('332', '0', '完成任务', '完成任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('333', '0', '关闭任务', '关闭任务', NULL);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (906,0,'任务管理 分类设置','任务管理 分类设置 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (907,0,'任务管理 统计','任务管理 统计 全部权限',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),904,24000,'查看所有任务',1,1,'查看所有任务 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),331,24000,'分派任务',0,1,'分派任务 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),332,24000,'完成任务',0,1,'完成任务 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),333,24000,'关闭任务',0,1,'关闭任务 全部权限',606);
  
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),905,25000,'服务类型设置',1,1,'服务类型设置 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),906,26000,'分类设置',1,1,'分类设置 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),907,27000,'统计',1,1,'统计 全部权限',606);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');

-- 物业保修2.0新增权限 by sunwen 20160923
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (805,0,'查看我的任务','任务管理 查看我的任务',NULL);
  
UPDATE eh_acl_privileges SET name = '查看所有任务', description = '任务管理 查看所有任务' WHERE id = 904;

UPDATE eh_web_menu_privileges SET name = '查看所有任务', discription = '查看所有任务 全部权限' where privilege_id = 904 and menu_id = 24000;

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),805,24000,'查看所有任务',1,1,'查看所有任务 全部权限',603);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1002,0,1,now());

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),420,37000,'车辆管理',1,1,'车辆管理  全部权限',370);


-- 新增客户资料和车辆管理菜单 by sfyan 20160923
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (37000,'客户资料管理',30000,null,'customer_management',0,2,'/30000/37000','park',370);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (38000,'车辆管理',30000,null,'car_management',0,2,'/50000/38000','park',380);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (420,0,'客户资料管理','客户资料管理 全部权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (421,0,'车辆管理','车辆管理 全部权限',null);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),420,37000,'客户资料管理',1,1,'客户资料管理  全部权限',200);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),421,38000,'车辆管理',1,1,'车辆管理  全部权限',201);


-- 新增消息推送和服务联盟 by sfyan 20160923
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44300,'消息推送设置',44000,null,'message_push_setting',0,2,'/44000/44300','park',458);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44400,'申请记录',44000,null,'apply_record',0,2,'/44000/44400','park',459);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (450,0,'消息推送设置','消息推送设置 全部权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (451,0,'申请记录','申请记录 全部权限',null);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),450,44300,'消息推送设置',1,1,'消息推送设置  全部权限',606);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),451,44400,'申请记录',1,1,'申请记录  全部权限',607);


-- 行业动态 by sfyan 20160926
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (340,0,'行业动态','行业动态 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11300,'行业动态',10000,null,'industry_dynamics',0,2,'/10000/11300','park',117);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),340,11300,'行业动态',1,1,'行业动态  全部权限',16);


-- 园区简介 by sfyan 20160926
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49700,'园区简介',40000,null,'settlement_management',0,2,'/40000/49700','park',455);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (570,0,'园区简介','园区简介 全部功能',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),570,49700,'园区简介',1,1,'园区简介',349);



-- 俱乐部 by tt, 20161117
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (571,0,'俱乐部管理','俱乐部管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (572,0,'审核俱乐部','审核俱乐部 全部功能',null);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49100,'俱乐部',40000,null,null,1,2,'/40000/49100','park',455);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49110,'俱乐部管理',49100,null,'groups_management',0,2,'/40000/49100/49110','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49120,'审核俱乐部',49100,null,'audit_groups',0,2,'/40000/49100/49120','park',457);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),571,49110,'俱乐部管理',1,1,'俱乐部管理  全部权限',350);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),572,49120,'审核俱乐部',1,1,'审核俱乐部 全部权限',351);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%49100/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%49100/%');

set @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT @scope_id := @scope_id+1, 49100,'', 'EhNamespaces', id , 2 FROM `eh_namespaces`;

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT @scope_id := @scope_id+1, 49110,'', 'EhNamespaces', id , 2 FROM `eh_namespaces`;

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT @scope_id := @scope_id+1, 49120,'', 'EhNamespaces', id , 2 FROM `eh_namespaces`;