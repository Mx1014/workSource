-- from orgChange by lei,lv 组织架构改动
DELETE from eh_organization_member_details;

-- detail表数据迁移脚本
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
    eom.group_type = 'ENTERPRISE' AND eo.parent_id = 0
GROUP BY
    eom.organization_id,
    eom.contact_token
ORDER BY
    eom.id;

-- 给member表的detail_id赋值
UPDATE eh_organization_members eom
SET eom.detail_id = (
    SELECT
        eomd.id
    FROM
        eh_organization_member_details eomd
    WHERE
        eomd.organization_id = SUBSTRING_index(
            SUBSTRING_index(eom.group_path, '/', 2),
            '/' ,- 1
        )
    AND eomd.contact_token = eom.contact_token
);

-- 同步user_organization脚本
DELETE
FROM
    eh_user_organizations;

SET @user_organization_id = 0;

INSERT INTO eh_user_organizations (
    id,
    namespace_id,
    user_id,
    organization_id,
    STATUS,
    group_type,
    group_path,
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
    eom.update_time,
    eom.visible_flag
FROM
    eh_organization_members eom
LEFT JOIN eh_organizations eo ON eom.organization_id = eo.id
WHERE
    eom.group_type = 'ENTERPRISE'
AND eom.target_type = 'USER'
AND (eom.status = '3' or eom.`status` = '1')
GROUP BY
    eom.organization_id,
    eom.contact_token
ORDER BY
    eom.id;

-- 资源预约 add by sw 20170808
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
	VALUES ('rental', '14000', 'zh_CN', '请补充线下模式负责人信息！');
