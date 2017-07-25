-- 用户申诉手机号成功消息内容  add by xq.tian  2017/07/12
SET @locale_max_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '3', 'zh_CN', '您修改手机号的申请已被拒绝，若有疑问请联系客服，感谢您的使用。');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300005', 'zh_CN', '请回到第一步重试');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300006', 'zh_CN', '验证码错误或已过期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300007', 'zh_CN', '审核申诉失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300008', 'zh_CN', '该手机号码已被注册');

-- 申诉短信模板 add by xq.tian  2017/07/12
SET @max_template_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-科技园', '90012', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-海岸', '90015', 999993);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-深业', '90018', 999992);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-威新', '90019', 999991);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-储能', '90020', 999990);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-ibase', '90021', 999989);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-爱特家', '90022', 999988);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-清华', '90091', 999984);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-创源', '90026', 999986);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-华润', '90027', 999985);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-T空间', '90028', 999982);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-正中会', '90029', 999983);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-星商汇', '90030', 999981);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-全至100', '90031', 999980);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-光大', '90034', 999979);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-Volgo', '90035', 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-名网邦', '90037', 999976);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 51, 'zh_CN', '申诉-荣超', '90038', 999975);
