
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
UPDATE eh_organizations SET directly_enterprise_id = parent_id WHERE group_type = 'JOB_LEVEL';

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
    IFNULL(eom.namespace_id, 0),
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
    NOW()
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
	IFNULL(eom.namespace_id, 0),
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
SET @id = 0;
INSERT INTO eh_rentalv2_price_rules 
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
SELECT @id:=@id+1,
         'default',
         t.id,
         t.rental_type,
         t.workday_price,
         t.weekend_price,
         t.org_member_workday_price,
         t.org_member_weekend_price,
         t.approving_user_workday_price,
         t.approving_user_weekend_price,
         NULL,
         NULL,
         NULL,
         NULL,
         0,
         0,
         0,
         NOW()
FROM eh_rentalv2_default_rules t
WHERE t.rental_type IS NOT NULL
UNION ALL
SELECT @id:=@id+1,
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
         NOW()
FROM eh_rentalv2_resources t2
WHERE t2.rental_type IS NOT NULL;

-- merge from energyqrcode by xiongying20170703
UPDATE eh_energy_meters m SET m.bill_category_id = (SELECT cs.id FROM eh_energy_meter_categories cs WHERE cs.name = 
    (SELECT c.name FROM eh_energy_meter_categories c WHERE c.id = m.bill_category_id) 
    AND cs.community_id = m.community_id AND cs.category_type = 1);
UPDATE eh_energy_meters m SET m.service_category_id = (SELECT cs.id FROM eh_energy_meter_categories cs WHERE cs.name = 
    (SELECT c.name FROM eh_energy_meter_categories c WHERE c.id = m.service_category_id) 
    AND cs.community_id = m.community_id AND cs.category_type = 2);
UPDATE eh_energy_meters m SET m.cost_formula_id = (SELECT fs.id FROM eh_energy_meter_formulas fs WHERE fs.name = 
    (SELECT f.name FROM eh_energy_meter_formulas f WHERE f.id = m.cost_formula_id) 
    AND fs.community_id = m.community_id AND fs.formula_type = 2);
UPDATE eh_energy_meters m SET m.amount_formula_id = (SELECT fs.id FROM eh_energy_meter_formulas fs WHERE fs.name = 
    (SELECT f.name FROM eh_energy_meter_formulas f WHERE f.id = m.amount_formula_id) 
    AND fs.community_id = m.community_id AND fs.formula_type = 1);

-- 短信黑名单  add by xq.tian  2017/07/04
SET @max_locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@max_locale_id := @max_locale_id + 1), 'user', '300004', 'zh_CN', '对不起，您的手机号在我们的黑名单列表');

-- 能耗换链接 add by xiongying20170705
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://core.zuolin.com/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' WHERE `item_label` LIKE '%能耗%';














-- 权限管理以及权限细化 merge wuyeprivilege  add by sfyan 20170705

-- delete from eh_service_modules where path like '%/20100/%';
-- delete from eh_service_modules where path like '%/20400/%';
DELETE FROM eh_service_modules WHERE path LIKE '%/20600/%';
DELETE FROM eh_service_modules WHERE path LIKE '%/20800/%';
-- delete from eh_service_modules where path like '%/20900/%';
DELETE FROM eh_service_modules WHERE path LIKE '%/21000/%';
-- delete from eh_service_modules where path like '%/49100/%';



INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20610,'类型管理',20600,'/20000/20600/20610','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20620,'规范管理',20600,'/20000/20600/20620','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20630,'标准管理',20600,'/20000/20600/20630','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20640,'标准审批',20600,'/20000/20600/20640','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20650,'任务查询',20600,'/20000/20600/20650','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20655,'绩效考核',20600,'/20000/20600/20655','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20660,'统计',20600,'/20000/20600/20660','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20661,'分数统计',20600,'/20000/20600/20660/20661','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20662,'任务数统计',20600,'/20000/20600/20660/20662','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20663,'检查统计',20600,'/20000/20600/20660/20663','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20670,'修改记录',20600,'/20000/20600/20670','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20810,'参考标准',20800,'/20000/20800/20810','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20811,'标准列表',20800,'/20000/20800/20810/20811','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20812,'巡检关联审批',20800,'/20000/20800/20810/20812','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20820,'巡检台账',20800,'/20000/20800/20820','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20821,'巡检对象',20800,'/20000/20800/20820/20821','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20822,'备品备件',20800,'/20000/20800/20820/20822','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20830,'任务列表',20800,'/20000/20800/20830','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20831,'任务列表',20800,'/20000/20800/20830/20831','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20840,'巡检项资料库管理',20800,'/20000/20800/20840','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20841,'巡检项设置',20800,'/20000/20800/20840/20841','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20850,'统计',20800,'/20000/20800/20850','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20851,'总览',20800,'/20000/20800/20850/20851','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20852,'查看所有任务',20800,'/20000/20800/20850/20852','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21010,'仓库维护',21000,'/20000/21000/21010','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21020,'物品维护',21000,'/20000/21000/21020','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21022,'物品信息',21000,'/20000/21000/21020/21022','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21024,'物品分类',21000,'/20000/21000/21020/21024','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21030,'库存维护',21000,'/20000/21000/21030','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21032,'库存查询',21000,'/20000/21000/21030/21032','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21034,'库存日志',21000,'/20000/21000/21030/21034','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21040,'领用管理',21000,'/20000/21000/21040','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21042,'领用管理',21000,'/20000/21000/21040/21042','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21044,'我的领用',21000,'/20000/21000/21040/21044','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21050,'参数配置',21000,'/20000/21000/21050','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21052,'工作流设置',21000,'/20000/21000/21050/21052','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21054,'参数配置',21000,'/20000/21000/21050/21054','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块



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

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'20600','1','10010','品质核查管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'20600','2','10010','品质核查全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'20800','1','10011','设备巡检管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'20800','2','10011','设备巡检全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id

UPDATE `eh_web_menus` SET `condition_type` = 'project' WHERE `path` LIKE '/20000/20600%';
UPDATE `eh_web_menus` SET `condition_type` = 'project' WHERE `path` LIKE '/20000/20800%';

-- 删除以前的通过授权规则授权的数据
DELETE FROM `eh_acls` WHERE scope LIKE '%.M%';

-- 删除以前的通过授权规则的数据
DELETE FROM `eh_service_module_assignments` WHERE relation_id = 0;

-- 补充菜单数据
UPDATE `eh_web_menus` SET LEVEL = (LENGTH(path)-LENGTH(REPLACE(path,'/','')));
UPDATE `eh_web_menus` SET `category` = 'module' WHERE LEVEL > 1;
UPDATE `eh_web_menus` SET `category` = 'classify' WHERE LEVEL = 1;

-- 补充超管数据
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid` , `create_time` , `namespace_id` , `role_type`) SELECT (@acl_id := @acl_id + 1),`owner_type`, `owner_id`,1,10, target_id, 0,1,NOW(),0, target_type FROM `eh_acl_role_assignments` WHERE role_id = 1001 AND target_type = 'EhUsers' AND target_id NOT IN (SELECT role_id FROM eh_acls WHERE role_type = 'EhUsers' AND privilege_id = 10);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid` , `create_time` , `namespace_id` , `role_type`) SELECT (@acl_id := @acl_id + 1),`owner_type`, `owner_id`,1,15, target_id, 0,1,NOW(),0, target_type FROM `eh_acl_role_assignments` WHERE role_id = 1005 AND target_type = 'EhUsers' AND target_id NOT IN (SELECT role_id FROM eh_acls WHERE role_type = 'EhUsers' AND privilege_id = 15);

-- 提示语 add by sfyan 20170705
SELECT MAX(id) INTO @id FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'privilege', '100051', 'zh_CN', '管理员已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'privilege', '100053', 'zh_CN', '管理员不存在');

-- 修改路由配置 add by sfyan 20170706
-- 修改路由配置 add by sfyan 20170706
UPDATE `eh_web_menus` SET data_type = 'react:/admin-management/admin-list' WHERE id = 60100;
UPDATE `eh_web_menus` SET data_type = 'react:/bussiness-authorization/authorization-list' WHERE id = 60200;
UPDATE `eh_service_modules` SET TYPE = 1 WHERE path LIKE '/10000%';
UPDATE `eh_service_modules` SET TYPE = 1 WHERE path LIKE '/20000%';
UPDATE `eh_service_modules` SET TYPE = 1 WHERE path LIKE '/30000%';
UPDATE `eh_service_modules` SET TYPE = 1 WHERE path LIKE '/40000%';
UPDATE `eh_service_modules` SET TYPE = 2 WHERE path LIKE '/50000%';
UPDATE `eh_service_modules` SET TYPE = 0 WHERE path LIKE '/60000%';


-- 停车充值 add by sw 20170706
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10005', 'zh_CN', '网络通讯失败，缴费出错');
UPDATE eh_parking_recharge_orders SET error_description = 'status状态是2,rechargestatus状态为1,付款成功,充值失败的老数据', STATUS = -1 WHERE STATUS = 2 AND recharge_status = 1;

UPDATE eh_parking_recharge_orders SET STATUS = 3 WHERE recharge_status = 2;

-- 不给单独授权的module状态置0 add by xiongying20170706
 
 
 
UPDATE eh_service_modules SET STATUS = 0 WHERE NAME = '巡检项资料库管理';
UPDATE eh_service_modules SET STATUS = 0 WHERE NAME = '巡检项设置';
UPDATE eh_service_modules SET STATUS = 0 WHERE NAME = '绩效考核';
UPDATE eh_service_modules SET STATUS = 0 WHERE NAME = '修改记录';
UPDATE eh_web_menus SET module_id = 20800 WHERE path = '/20000/20800/20850';
UPDATE eh_web_menus SET module_id = 20800 WHERE path = '/20000/20800/20850/20851';
UPDATE eh_web_menus SET module_id = 20800 WHERE path = '/20000/20800/20850/20852';
UPDATE eh_web_menus SET module_id = 49100 WHERE path LIKE '%49100%'; 


-- added by wh  邮件用html格式
UPDATE eh_locale_templates SET TEXT = '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>${title}</title></head><body><p>尊敬的${nickName}：<br>
您好，感谢您使用${appName}，点击下面的链接进行邮箱验证：<br>
<a href="${verifyUrl}">点我验证</a> <br>
如果链接没有跳转，请直接复制链接地址到您的浏览器地址栏中访问。（30分钟内有效）
 <br>
此邮件为系统邮件，请勿直接回复。
<br>
如非本人操作，请忽略此邮件。
<br>
谢谢，${appName}</p></body></html>'
WHERE scope = 'verify.mail' AND CODE =1 ;



-- 菜单整理脚本 add by sfyan 20170706
-- 给菜单补上moduleid
UPDATE `eh_web_menus` SET module_id = REVERSE(SUBSTRING_INDEX(REVERSE(SUBSTRING_INDEX(path, "/", 3)), "/", 1)) WHERE path LIKE '/20000/%' AND module_id IS NULL; 
UPDATE `eh_web_menus` SET module_id = REVERSE(SUBSTRING_INDEX(REVERSE(SUBSTRING_INDEX(path, "/", 3)), "/", 1))  WHERE path LIKE '/40000/%' AND module_id IS NULL;

-- 屏蔽掉物业公司不要的菜单
UPDATE eh_web_menus SET STATUS = 0 WHERE ID IN (SELECT WW.ID FROM (SELECT ID FROM eh_web_menus WHERE ID NOT IN (SELECT ID FROM `eh_web_menus` WHERE id IN (SELECT menu_id FROM `eh_web_menu_privileges` WHERE privilege_id IN (SELECT privilege_id FROM eh_acls WHERE role_id = 1001 AND privilege_id > 10000)) OR id IN (10000,20000,30000,40000,50000,40700,50000,60000,70000,80000,41200,70000))) WW);

-- 新配置给普通企业的菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) SELECT CONCAT(id,'0'),`name`, CONCAT(`parent_id`,'0'), `icon_url`, `data_type`, `leaf_flag`, `status`, CONCAT(RIGHT(REPLACE(path, '/', '0/'), LENGTH(REPLACE(path, '/', '0/')) - 1), '0'), 'organization', `sort_num`, `module_id`, `level`, `condition_type`, `category` FROM `eh_web_menus` WHERE id IN (SELECT menu_id FROM `eh_web_menu_privileges` WHERE privilege_id IN (SELECT privilege_id FROM eh_acls WHERE role_id = 1005 AND privilege_id > 10000)) OR id IN (20000,20600,20660,40000,40700,50000,60000,70000,80000);

-- 给域空间的普通企业配置菜单
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1), CONCAT(menu_id, '0'), `menu_name`, `owner_type`, `owner_id`, `apply_policy` FROM eh_web_menu_scopes WHERE menu_id IN (SELECT id FROM `eh_web_menus` WHERE id IN (SELECT menu_id FROM `eh_web_menu_privileges` WHERE privilege_id IN (SELECT privilege_id FROM eh_acls WHERE role_id = 1005 AND privilege_id > 10000)) OR id IN (20000,20600,20660,40000,40700,50000,60000,70000,80000));

