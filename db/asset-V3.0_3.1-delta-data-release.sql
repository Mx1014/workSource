-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 唐岑 2018年8月5日15:50:45
-- REMARK: 
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20007', 'zh_CN', '楼层数填写格式错误，楼层数只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20016', 'zh_CN', '楼层数填写格式错误，楼层数只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '10201', 'zh_CN', '楼宇名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30001', 'zh_CN', '建筑面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30002', 'zh_CN', '收费面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30003', 'zh_CN', '在租面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30004', 'zh_CN', '可招租面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30005', 'zh_CN', '楼层不在该楼宇楼层范围内');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '10012', 'zh_CN', '楼宇编号已存在');
------------------------ SECTION END ---------------------------------------------------------
