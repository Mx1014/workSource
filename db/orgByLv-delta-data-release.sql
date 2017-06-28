-- 1.运行建表脚本
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_organization_member_details
-- ----------------------------
DROP TABLE IF EXISTS `eh_organization_member_details`;
CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL COMMENT 'id for members， reference for eh_organization_member detail_id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `birthday` DATE COMMENT 'the birthday of the member',
  `organization_id` BIGINT NOT NULL COMMENT 'reference for eh_organization_member organization_id' ,
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `contact_description` TEXT,
  `employee_no` VARCHAR(128) COMMENT 'the employee number for the member',
  `avatar` varchar(128) COMMENT '头像',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `marital_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: undisclosured, 1: married, 2: unmarried',
  `political_status` VARCHAR(128) COMMENT '政治面貌',
  `native_place` VARCHAR(128) COMMENT '籍贯',
  `en_name` VARCHAR(128) COMMENT 'english name',
  `reg_residence` VARCHAR(128) COMMENT '户口',
  `id_number` VARCHAR(64) COMMENT 'ID Card number',
  `email` VARCHAR(128) COMMENT 'email for members',
  `wechat` VARCHAR(128),
  `qq` VARCHAR(128),
  `emergency_name` VARCHAR(128) COMMENT 'emergency contact name',
  `emergency_contact` VARCHAR(128) COMMENT 'emergency contact tel-number',
  `address` VARCHAR(255) COMMENT 'address for the member',
  `employee_type` TINYINT COMMENT '0: full-time, 1: part-time, 2: internship, 3: labor dispatch',
  `employee_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: probation, 1: on the job, 2: leave the job',
  `employment_time` DATE COMMENT '转正日期',
  `dimission_time` DATE COMMENT '离职日期',
  `salary_card_number` VARCHAR(128) COMMENT '工资卡号',
  `social_security_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `profile_integrity` INTEGER DEFAULT 0 COMMENT '档案完整度，0-100%',
  `check_in_time` DATE NOT NULL COMMENT '入职日期',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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