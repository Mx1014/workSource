-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 唐岑 2018年7月13日15:10:36
-- REMARK: 房源拆分合并计划的备注模板
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '1', 'zh_CN', '房源拆分计划', '${originalIds}被拆分成${targetIds}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '2', 'zh_CN', '房源合并计划', '${originalIds}被合并成${targetIds}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '3', 'zh_CN', '新（中文）', '新', '0');
-- --------------------- SECTION END ---------------------------------------------------------
