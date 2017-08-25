
-- merge 4.8.1 迁移过来且还未上线的脚本  add by sfyan 20170821

--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);


-- 以上是4.8.1的脚本

-- merge from profile-1.2 started by R
SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '900024', 'zh_CN', '性别仅支持"男""女"');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '900025', 'zh_CN', '姓名长度需小于20个字');
-- merge from profile-1.2 ended by R


-- 【正中会】【华润】电商链接组件配置 add by sfyan 20170825
update eh_launch_pad_layouts set `layout_json` = '{"versionCode":"2017082501","versionName":"3.12.2","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Gallery1"},"style":"Gallery","defaultOrder":4,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Gallery"},"style":"Gallery","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,"columnCount":3},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"OPPushBiz","entityCount":6,"subjectHeight":1,"descriptionHeight":0},"style":"HorizontalScrollView","defaultOrder":6,"separatorFlag":0,"separatorHeight":0,"columnCount":0}]}', `version_code` = 2017082501 where namespace_id = 999983 and name = 'ServiceMarketLayout';
update `eh_launch_pad_items` set action_data = 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId={mallId}&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fclientrecommend%3d1%23%2frecommend%2flist%2f111%3f_k%3dzlbiz#sign_suffix' where namespace_id = 999983 and item_group = 'OPPushBiz';
update `eh_launch_pad_items` set action_data = 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId={mallId}&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fclientrecommend%3d1%23%2frecommend%2f1448194692979480604%2flist%3f_k%3dzlbiz#sign_suffix' where namespace_id = 999985 and item_group = 'OPPushBiz';
