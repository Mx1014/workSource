-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: huangpengyu  20180709
-- REMARK: 此功能将所有不需要走审批的表单字段关联起来
create table `eh_general_form_val_requests`
(
   `id`                   bigint not null,
   `organization_id`      bigint comment 'organization_id',
   `owner_id`             bigint comment 'owner_id',
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id',
   `module_id`            bigint comment 'module_id',
   `module_type`          varchar(64) comment 'module_type',
   `source_id`            bigint comment 'source_id',
   `source_type`          varchar(64) comment 'source_type',
	 `approval_status`      tinyint comment '该表单的审批状态,0-待发起，1-审批中，2-审批通过，3-审批终止' default 0,
	 `status`               tinyint comment '该表单的状态，0-删除，1-生效' default 1,
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_val_requests in dev mode';

-- AUTHOR: huangpengyu  20180717
-- REMARK: 将表单筛选字段与客户关联起来
create table `eh_general_form_filter_user_map`
(
   `id`                   bigint not null,
   `owner_id`             bigint comment 'owner_id' not null,
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id' not null,
   `module_id`            bigint comment 'module_id' not null,
   `module_type`          varchar(64) comment 'module_type' ,
	 `form_origin_id`				bigint comment '关联的表id' not null,
	 `form_version`					bigint comment '关联的表version' not null,
   `field_name`           varchar(64) comment '被选中的字段名' not null,
	 `user_uuid`						varchar(128) comment '当前登录的用户用于获取字段' not null,
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_filter_user_map in dev mode';


alter table eh_enterprise_customers add aptitude_flag_item_id BIGINT null comment '0-无资质，1-有资质' default 0;
alter table eh_contract_categories add aptitude_flag BIGINT null comment '0-不过滤，1-过滤' default 0;

alter table eh_general_forms add operator_name varchar(64) null comment '修改人';
alter table eh_general_forms add creater_name varchar(64) null comment '新增人';


alter table eh_general_approvals add creater_name varchar(64) null comment '新增人';

-- AUTHOR: huangpengyu  20180811
-- REMARK: 增加合同过滤客户配置项
create table `eh_enterprise_customer_aptitude_flag`
(
   `id`                   bigint not null,
   `value`             		TINYINT not null comment '是否筛选，1-筛选，0-不筛选' default 0,
	 `owner_id`           	bigint not null comment 'communityId',
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id',
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_customer_aptitude_flag in dev mode';


-- 2018年8月22日
-- 新增在request表单中存储该表单的formOriginId和formVersion
alter table eh_general_form_val_requests add form_origin_id BIGINT null comment '该表单所属的表单模板id';
alter table eh_general_form_val_requests add form_version BIGINT null comment '该表单所属的表单模板version';


-- --------------------- SECTION END ---------------------------------------------------------