-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 缪洲 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10034', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10035', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10036', 'zh_CN', '订单状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10037', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10038', 'zh_CN', '工作流未开启');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10039', 'zh_CN', '对象不存在');

-- AUTHOR: 马世亨 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10020','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10021','zh_CN','查询失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10022','zh_CN','文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10023','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10024','zh_CN','第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10025','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1414','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1415','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1416','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1417','zh_CN','二维码下载失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1418','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1419','zh_CN','文件导出失败');

-- by st.zheng
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'relocation', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '507', 'zh_CN', '参数缺失', '参数缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '508', 'zh_CN', '资源或资源规则缺失', '资源或资源规则缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '509', 'zh_CN', '找不到订单或订单状态错误', '找不到订单或订单状态错误', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '510', 'zh_CN', '下单失败', '下单失败', '0');



-- AUTHOR: 黄明波 20181008
-- REMARK: issue-38650 增加error消息模板
-- yellowPage
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10012', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10013', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10014', 'zh_CN', '跳转链接格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10015', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10016', 'zh_CN', '获取电商模块失败');


-- express
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10003', 'zh_CN', 'URL加密失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10004', 'zh_CN', '请求失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10005', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10006', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10007', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10008', 'zh_CN', '获取公司失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10009', 'zh_CN', '用户鉴权失败，请重新登录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10010', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10011', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10012', 'zh_CN', '支付鉴权失败');

-- news
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10017', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10018', 'zh_CN', '接口参数异常');

-- print

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10000', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10001', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10002', 'zh_CN', '邮箱地址格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10003', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10004', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10005', 'zh_CN', '获取打印任务失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10006', 'zh_CN', '订单不存在或已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10007', 'zh_CN', '打印机解锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10008', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10009', 'zh_CN', '扫码失败，请重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10010', 'zh_CN', '有未支付订单，请支付后重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10011', 'zh_CN', '订单已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10012', 'zh_CN', '锁定订单失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10013', 'zh_CN', '文件导出失败');




-- --------------------- SECTION END ---------------------------------------------------------


