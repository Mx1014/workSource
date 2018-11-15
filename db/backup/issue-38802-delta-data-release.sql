-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 梁燕龙
-- REMARK: issue-38802
INSERT INTO `eh_locale_strings` (scope, code, locale, text)
    VALUES ('activity', 10033,'zh_CN','请到活动详情页扫码签到');

-- --------------------- SECTION END ---------------------------------------------------------


