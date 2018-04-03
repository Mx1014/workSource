-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type tinyint(4) DEFAULT 0;