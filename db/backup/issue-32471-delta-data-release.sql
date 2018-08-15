-- 通用脚本
-- ADD BY 张智伟
-- issue-32471 考勤加班规则不同加班类型的提示文案初始化
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
 
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', -1, 'zh_CN', '未设置打卡规则');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 0, 'zh_CN', '未开启');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 1, 'zh_CN', '需要申请和打卡，时长按打卡时间计算，但不能超过申请的时间');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 2, 'zh_CN', '需要申请，时长按申请单时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 3, 'zh_CN', '不需要申请，时长按打卡时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 4, 'zh_CN', '工作日加班：');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 5, 'zh_CN', '休息日/节假日加班：');

-- 初始化2018剩余节假日 
UPDATE eh_punch_holidays SET legal_flag = 1 WHERE rule_date IN ('2018-01-01','2018-02-16','2018-02-17','2018-02-18','2018-04-05','2018-05-01','2018-06-18','2018-09-24','2018-10-01','2018-10-02','2018-10-03');

-- 通用脚本
-- ADD BY 吴寒
-- 审批类型的的一些locale_string
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'ASK_FOR_LEAVE', 'zh_CN', '请假'); 
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'BUSINESS_TRIP', 'zh_CN', '出差'); 
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'OVERTIME', 'zh_CN', '加班'); 
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'GO_OUT', 'zh_CN', '外出');  