-- 插入审批类型对应的汉字文本，add by tt, 20160901
SET @max_id = (SELECT max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '1', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '2', 'zh_CN', '异常');

set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10001', 'zh_CN', '请输入请假理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10002', 'zh_CN', '请选择请假时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10003', 'zh_CN', '请假开始时间必须小于请假结束时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10004', 'zh_CN', '所选时间包含已请假时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10005', 'zh_CN', '请假时间是非工作时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10006', 'zh_CN', '请假时长为0');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10007', 'zh_CN', '请输入申请理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10008', 'zh_CN', '类型名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10009', 'zh_CN', '类型名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10010', 'zh_CN', '该审批人设置关联了审批规则，请先删除对应的审批规则，再删除该审批人设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10011', 'zh_CN', '审批名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10012', 'zh_CN', '审批名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10013', 'zh_CN', '审批名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10014', 'zh_CN', '审批规则名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10015', 'zh_CN', '审批规则名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10016', 'zh_CN', '审批规则名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10017', 'zh_CN', '请假审批不能选择无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10018', 'zh_CN', '忘打卡审批不能为无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10019', 'zh_CN', '请输入驳回理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10020', 'zh_CN', '请选择需要审批的申请单');