-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇
-- REMARK: 防止模板表单占用

update `eh_general_form_templates` set namespace_id = 0, id = 2500001 where module_id = 25000;

-- END