-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
UPDATE eh_service_modules SET multiple_flag = 1 WHERE id = 10400;
-- 刷app值
UPDATE eh_service_module_apps SET instance_config = '{"categoryId":0}' WHERE module_id = 10400;
-- 刷广告数据入口
UPDATE eh_banners SET category_id = 0;