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
   primary key (id)
);ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_val_requests in dev mode';

-- --------------------- SECTION END ---------------------------------------------------------