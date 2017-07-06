
-- merge from forum-2.0 start by yanjun 20170703
-- 增加话题、投票类型的热门标签  add by yanjun 20170613
SET @eh_hot_tags_id = (SELECT MAX(id) FROM eh_hot_tags);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','二手和租售','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','免费物品','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','失物招领','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','紧急通知','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','创意','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','人气','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','节日','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','兴趣','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','讨论','1','0',NOW(),'1',NULL,NULL);

-- 修改搜索的页面图标的显示顺序，话题第一、投票第二 add by yanjun 20170627
UPDATE eh_search_types SET `order`  = 1 WHERE content_type = 'topic';
UPDATE eh_search_types SET `order`  = 2 WHERE content_type = 'activity';
-- merge from forum-2.0 end by yanjun 20170703

-- merge from orgByLv started by rongnan 20170703
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900001', 'zh_CN', '部门不存在');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900002', 'zh_CN', '岗位不存在');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900003', 'zh_CN', '职级不存在');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900004', 'zh_CN', '性别未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900005', 'zh_CN', '部门未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900006', 'zh_CN', '岗位未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900007', 'zh_CN', '入职日期为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900008', 'zh_CN', '试用期未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900009', 'zh_CN', '转正日期未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900010', 'zh_CN', '电话号码格式错误');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900011', 'zh_CN', '学校名称为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900012', 'zh_CN', '学历为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900013', 'zh_CN', '专业为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900014', 'zh_CN', '起始日期未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900015', 'zh_CN', '结束日期未填写');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900016', 'zh_CN', '公司名称为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900017', 'zh_CN', '职位为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900018', 'zh_CN', '职位类型为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900019', 'zh_CN', '保险名称为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900020', 'zh_CN', '保险公司为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900021', 'zh_CN', '保险编号为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900022', 'zh_CN', '劳动合同编号为空');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`)
VALUES ('organization', '900023', 'zh_CN', '日期格式错误');
-- merge from orgByLv ended by rongnan 20170703

-- merge from orgByLv started by lei.lv 20170703
-- 1.运行建表脚本
-- 2.运行新增字段脚本
-- 4.修复organization表中group_type = 'JOB_LEVEL'的记录没有directly_enterprise_id的问题
UPDATE eh_organizations set directly_enterprise_id = parent_id WHERE group_type = 'JOB_LEVEL';

-- 5.运行数据迁移脚本

SET @detail_id = 0;

INSERT INTO eh_organization_member_details (
    id,
    namespace_id,
    target_type,
    target_id,
    organization_id,
    contact_name,
    contact_type,
    contact_token,
    employee_no,
    avatar,
    gender,
    contact_description,
    check_in_time
) SELECT
    (@detail_id := @detail_id + 1),
    ifnull(eom.namespace_id, 0),
    eom.target_type,
    eom.target_id,
    eom.organization_id,
    eom.contact_name,
    eom.contact_type,
    eom.contact_token,
    eom.employee_no,
    eom.avatar,
    eom.gender,
    eom.contact_description,
    now()
FROM
    eh_organization_members eom
LEFT JOIN eh_organizations eo ON eom.organization_id = eo.id
WHERE
    eom.group_type = 'ENTERPRISE'
GROUP BY
    eom.organization_id,
    eom.contact_token
ORDER BY
    eom.id;

 -- 6.给member表的detail_id赋值
UPDATE eh_organization_members eom
SET eom.detail_id = (
    SELECT
        eomd.id
    FROM
        eh_organization_member_details eomd
    WHERE
        (
            eomd.organization_id = (
                SELECT
                    eo.directly_enterprise_id
                FROM
                    eh_organizations eo
                WHERE
                    eo.id = eom.organization_id
            )
            AND eom.contact_token = eomd.contact_token
            AND eom.group_type != 'ENTERPRISE'
        )
    OR (
        eomd.organization_id = eom.organization_id
        AND eom.group_type = 'ENTERPRISE'
        AND eomd.contact_token = eom.contact_token
    )
);

-- 初始化eh_user_organizations的数据

SET @user_organization_id = 0;

INSERT INTO eh_user_organizations (
	id,
	namespace_id,
	user_id,
	organization_id,
	STATUS,
	group_type,
	group_path,
	create_time,
	update_time,
	visible_flag
) SELECT
	(
		@user_organization_id := @user_organization_id + 1
	),
	ifnull(eom.namespace_id, 0),
	eom.target_id,
	eom.organization_id,
	eom. STATUS,
	eom.group_type,
	eom.group_path,
	eom.create_time,
	eom.update_time,
	eom.visible_flag
FROM
	eh_organization_members eom
LEFT JOIN eh_organizations eo ON eom.organization_id = eo.id
WHERE
	eom.group_type = 'ENTERPRISE'
AND eom.target_type = 'USER'
GROUP BY
	eom.organization_id,
	eom.contact_token
ORDER BY
	eom.id;
-- merge from orgByLv end by lei.lv 20170703-- merge from forum-2.0 end by yanjun 20170703

-- 初始化数据，add by tt, 20170703
set @id = 0;
insert into eh_rentalv2_price_rules 
(
    `id`,
    `owner_type`,
    `owner_id`,
    `rental_type`,
    `workday_price`,
    `weekend_price`,
    `org_member_workday_price`,
    `org_member_weekend_price`,
    `approving_user_workday_price`,
    `approving_user_weekend_price`, 
    `discount_type`,
    `full_price`,
    `cut_price`,
    `discount_ratio`,
    `cell_begin_id`,
    `cell_end_id`,
    `creator_uid`,
    `create_time`
)
select @id:=@id+1,
         'default',
         t.id,
         t.rental_type,
         t.workday_price,
         t.weekend_price,
         t.org_member_workday_price,
         t.org_member_weekend_price,
         t.approving_user_workday_price,
         t.approving_user_weekend_price,
         null,
         null,
         null,
         null,
         0,
         0,
         0,
         now()
from eh_rentalv2_default_rules t
where t.rental_type is not null
union all
select @id:=@id+1,
         'resource',
         t2.id,
         t2.rental_type,
         t2.workday_price,
         t2.weekend_price,
         t2.org_member_workday_price,
         t2.org_member_weekend_price,
         t2.approving_user_workday_price,
         t2.approving_user_weekend_price,
         t2.discount_type,
         t2.full_price,
         t2.cut_price,
         t2.discount_ratio,
         t2.cell_begin_id,
         t2.cell_end_id,
         0,
         now()
from eh_rentalv2_resources t2
where t2.rental_type is not null;

-- merge from energyqrcode by xiongying20170703
update eh_energy_meters m set m.bill_category_id = (select cs.id from eh_energy_meter_categories cs where cs.name = 
    (select c.name from eh_energy_meter_categories c where c.id = m.bill_category_id) 
    and cs.community_id = m.community_id and cs.category_type = 1);
update eh_energy_meters m set m.service_category_id = (select cs.id from eh_energy_meter_categories cs where cs.name = 
    (select c.name from eh_energy_meter_categories c where c.id = m.service_category_id) 
    and cs.community_id = m.community_id and cs.category_type = 2);
update eh_energy_meters m set m.cost_formula_id = (select fs.id from eh_energy_meter_formulas fs where fs.name = 
    (select f.name from eh_energy_meter_formulas f where f.id = m.cost_formula_id) 
    and fs.community_id = m.community_id and fs.formula_type = 2);
update eh_energy_meters m set m.amount_formula_id = (select fs.id from eh_energy_meter_formulas fs where fs.name = 
    (select f.name from eh_energy_meter_formulas f where f.id = m.amount_formula_id) 
    and fs.community_id = m.community_id and fs.formula_type = 1);

-- 短信黑名单  add by xq.tian  2017/07/04
SET @max_locale_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@max_locale_id := @max_locale_id + 1), 'user', '300004', 'zh_CN', '对不起，您的手机号在我们的黑名单列表');

-- 能耗换链接 add by xiongying20170705
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://core.zuolin.com/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' WHERE `item_label` LIKE '%能耗%';














-- 权限管理以及权限细化 merge wuyeprivilege  add by sfyan 20170705

-- delete from eh_service_modules where path like '%/20100/%';
-- delete from eh_service_modules where path like '%/20400/%';
delete from eh_service_modules where path like '%/20600/%';
delete from eh_service_modules where path like '%/20800/%';
-- delete from eh_service_modules where path like '%/20900/%';
delete from eh_service_modules where path like '%/21000/%';
-- delete from eh_service_modules where path like '%/49100/%';



insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20610,'类型管理',20600,'/20000/20600/20610','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20620,'规范管理',20600,'/20000/20600/20620','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20630,'标准管理',20600,'/20000/20600/20630','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20640,'标准审批',20600,'/20000/20600/20640','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20650,'任务查询',20600,'/20000/20600/20650','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20655,'绩效考核',20600,'/20000/20600/20655','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20660,'统计',20600,'/20000/20600/20660','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20661,'分数统计',20600,'/20000/20600/20660/20661','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20662,'任务数统计',20600,'/20000/20600/20660/20662','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20663,'检查统计',20600,'/20000/20600/20660/20663','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20670,'修改记录',20600,'/20000/20600/20670','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20810,'参考标准',20800,'/20000/20800/20810','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20811,'标准列表',20800,'/20000/20800/20810/20811','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20812,'巡检关联审批',20800,'/20000/20800/20810/20812','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20820,'巡检台账',20800,'/20000/20800/20820','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20821,'巡检对象',20800,'/20000/20800/20820/20821','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20822,'备品备件',20800,'/20000/20800/20820/20822','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20830,'任务列表',20800,'/20000/20800/20830','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20831,'任务列表',20800,'/20000/20800/20830/20831','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20840,'巡检项资料库管理',20800,'/20000/20800/20840','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20841,'巡检项设置',20800,'/20000/20800/20840/20841','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20850,'统计',20800,'/20000/20800/20850','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20851,'总览',20800,'/20000/20800/20850/20851','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20852,'查看所有任务',20800,'/20000/20800/20850/20852','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21010,'仓库维护',21000,'/20000/21000/21010','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21020,'物品维护',21000,'/20000/21000/21020','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21022,'物品信息',21000,'/20000/21000/21020/21022','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21024,'物品分类',21000,'/20000/21000/21020/21024','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21030,'库存维护',21000,'/20000/21000/21030','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21032,'库存查询',21000,'/20000/21000/21030/21032','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21034,'库存日志',21000,'/20000/21000/21030/21034','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21040,'领用管理',21000,'/20000/21000/21040','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21042,'领用管理',21000,'/20000/21000/21040/21042','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21044,'我的领用',21000,'/20000/21000/21040/21044','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21050,'参数配置',21000,'/20000/21000/21050','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21052,'工作流设置',21000,'/20000/21000/21050/21052','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21054,'参数配置',21000,'/20000/21000/21050/21054','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块



SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.manage', 10010, '', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.all', 10010, '', '0', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准审批查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standardreview.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20640','0',@privilege_id,'品质核查 标准审批查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准审批审核权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standardreview.review', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20640','0',@privilege_id,'品质核查 标准审批审核权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 任务查询权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.task.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20650','0',@privilege_id,'品质核查 任务查询权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 分数统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.score', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20660','0',@privilege_id,'品质核查 分数统计查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 任务数统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.task', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20660','0',@privilege_id,'品质核查 任务数统计查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 修改记录查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.updatelog.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20670','0',@privilege_id,'品质核查 修改记录查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
     VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
     VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
     VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
     VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 检查统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.sample', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
     VALUES((@module_privilege_id := @module_privilege_id + 1),'20660','0',@privilege_id,'品质核查 检查统计查看权限','0',NOW());

  
  
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.manage', 10011, '', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.all', 10011, '', '0', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`) 
    VALUES ('20811', '参考标准增删改查', '20800', '/20000/20800/20811', '1', '3', '0', '0', '2017-06-23 10:03:23', NULL, NULL, '0', '0', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准新增修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准新增修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检关联审批审核权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.relation.review', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20810','0',@privilege_id,'设备巡检 巡检关联审批审核权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检关联审批删除失效关联权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.relation.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20810','0',@privilege_id,'设备巡检 巡检关联审批删除失效关联权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20820','0',@privilege_id,'设备巡检 巡检对象查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象新增修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20820','0',@privilege_id,'设备巡检 巡检对象新增修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20820','0',@privilege_id,'设备巡检 巡检对象删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 任务查询权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.task.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20830','0',@privilege_id,'设备巡检 任务查询权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20840','0',@privilege_id,'设备巡检 巡检项查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项新增权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20840','0',@privilege_id,'设备巡检 巡检项新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20840','0',@privilege_id,'设备巡检 巡检项删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20840','0',@privilege_id,'设备巡检 巡检项修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 统计总览权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.stat.pandect', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20850','0',@privilege_id,'设备巡检 统计总览权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 统计查看所有任务权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.stat.alltask', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20850','0',@privilege_id,'设备巡检 统计查看所有任务权限','0',NOW());

insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20600','1','10010','品质核查管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20600','2','10010','品质核查全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id

insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20800','1','10011','设备巡检管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20800','2','10011','设备巡检全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id

update `eh_web_menus` set `condition_type` = 'project' where `path` like '/20000/20600%';
update `eh_web_menus` set `condition_type` = 'project' where `path` like '/20000/20800%';

-- 删除以前的通过授权规则授权的数据
delete from `eh_acls` where scope like '%.M%';

-- 删除以前的通过授权规则的数据
delete from `eh_service_module_assignments` where relation_id = 0;

-- 补充菜单数据
update `eh_web_menus` set level = (length(path)-length(replace(path,'/','')));
update `eh_web_menus` set `category` = 'module' where level > 1;
update `eh_web_menus` set `category` = 'classify' where level = 1;

-- 补充超管数据
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
insert into `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid` , `create_time` , `namespace_id` , `role_type`) select (@acl_id := @acl_id + 1),`owner_type`, `owner_id`,1,10, target_id, 0,1,now(),0, target_type from `eh_acl_role_assignments` where role_id = 1001 and target_type = 'EhUsers' and target_id not in (select role_id from eh_acls where role_type = 'EhUsers' and privilege_id = 10);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
insert into `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid` , `create_time` , `namespace_id` , `role_type`) select (@acl_id := @acl_id + 1),`owner_type`, `owner_id`,1,15, target_id, 0,1,now(),0, target_type from `eh_acl_role_assignments` where role_id = 1005 and target_type = 'EhUsers' and target_id not in (select role_id from eh_acls where role_type = 'EhUsers' and privilege_id = 15);

-- 提示语 add by sfyan 20170705
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'privilege', '100051', 'zh_CN', '管理员已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'privilege', '100053', 'zh_CN', '管理员不存在');

-- 修改路由配置 add by sfyan 20170706
-- 修改路由配置 add by sfyan 20170706
update `eh_web_menus` set data_type = 'react:/admin-management/admin-list' where id = 60100;
update `eh_web_menus` set data_type = 'react:/bussiness-authorization/authorization-list' where id = 60200;
update `eh_service_modules` set type = 1 where path like '/10000%';
update `eh_service_modules` set type = 1 where path like '/20000%';
update `eh_service_modules` set type = 1 where path like '/30000%';
update `eh_service_modules` set type = 1 where path like '/40000%';
update `eh_service_modules` set type = 2 where path like '/50000%';
update `eh_service_modules` set type = 0 where path like '/60000%';


-- 停车充值 add by sw 20170706
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10005', 'zh_CN', '网络通讯失败，充值出错');
UPDATE eh_parking_recharge_orders set status = 3 where recharge_status = 2;

-- 不给单独授权的module状态置0 add by xiongying20170706
update eh_service_modules set status = 0 where name = '巡检项资料库管理';
update eh_service_modules set status = 0 where name = '巡检项设置';
update eh_service_modules set status = 0 where name = '绩效考核';
update eh_service_modules set status = 0 where name = '修改记录';
update eh_web_menus set module_id = 20800 where path = '/20000/20800/20850';
update eh_web_menus set module_id = 20800 where path = '/20000/20800/20850/20851';
update eh_web_menus set module_id = 20800 where path = '/20000/20800/20850/20852';
update eh_web_menus set module_id = 49100 where path like '%49100%';
