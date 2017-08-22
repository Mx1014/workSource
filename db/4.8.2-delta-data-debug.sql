
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