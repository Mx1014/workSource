-- 20180528-huangliangming-配置项管理-#30016
-- 初始化配置项表“是否只读”字段为“是”，值为1
UPDATE eh_configurations s SET s.is_readonly = 1 ;

-- 20180522-huangliangming