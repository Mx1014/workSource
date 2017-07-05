
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