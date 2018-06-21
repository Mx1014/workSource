-- 通用脚本
-- ADD BY 黄良铭
-- issue-30013 初始化短信白名单配置项
-- 初始化配置项表“是否只读”字段为“是”，值为1
UPDATE eh_configurations s SET s.is_readonly = 1 ;

-- END BY 黄良铭