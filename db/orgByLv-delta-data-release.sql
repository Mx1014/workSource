-- 1.运行建表脚本
--2.运行新增字段脚本
ALTER TABLE `eh_organization_members` ADD COLUMN `detail_id` BIGINT COMMENT 'id for detail records'

--3.备份eh_organization_members表为eh_organization_members_temp
create table eh_organization_members_temp select * from eh_organization_members

--4.修复organization表中group_type = 'JOB_LEVEL'的记录没有directly_enterprise_id的问题
UPDATE eh_organizations set directly_enterprise_id = parent_id WHERE group_type = 'JOB_LEVEL'

--5.运行数据迁移脚本

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
)

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