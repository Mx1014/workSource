-- 增加域空间左上角显示场景名称的配置项

ALTER TABLE eh_namespace_details ADD COLUMN name_type TINYINT(4) DEFAULT 0;

-- fix 15631 & 15636 add by xiongying20170919
ALTER TABLE eh_organizations ADD COLUMN website VARCHAR(256);
ALTER TABLE eh_organizations ADD COLUMN unified_social_credit_code VARCHAR(256);

-- 同步数据时为企业添加门牌时太慢，添加索引 add by xiongying20170922
ALTER TABLE `eh_addresses` ADD INDEX namespace_address ( `namespace_address_type`, `namespace_address_token`);
