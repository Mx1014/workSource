-- 张江高科现在可以展示批量导入导出的按钮了
delete from eh_service_module_exclude_functions where module_id = 20400 and function_id = 95 and namespace_id = 999971;
