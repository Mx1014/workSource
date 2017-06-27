-- 用户申诉手机号成功消息内容  add by xq.tian  2017/06/30
SET @locale_max_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '3', 'zh_CN', '您修改手机号的申请已被拒绝，若有疑问请联系客服，感谢您的使用。');
