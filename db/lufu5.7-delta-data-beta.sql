-- 用于测试，不放入release中
update eh_launch_pad_items set item_name='物业查费' where id = '149688';
update eh_launch_pad_items set item_label='物业查费' where id = '149688';
update eh_launch_pad_items set 
action_data='{"url":"${home.url}/evh/openapi/redirect?url=http://202.96.185.82:8088/feesrv.html&handler=LUFU#sign_suffix"}' where id = '149688';

update eh_launch_pad_items set item_name='物业查费' where id = '149689';
update eh_launch_pad_items set item_label='物业查费' where id = '149689';
update eh_launch_pad_items set 
action_data='{"url":"${home.url}/evh/openapi/redirect?url=http://202.96.185.82:8088/feesrv.html&handler=LUFU#sign_suffix"}' where id = '149689';

update eh_organization_member_details set contact_token='15018490384' where id = '30645';
update eh_user_identifiers set identifier_token='15018490384' where id = '448303';
update eh_organization_members set contact_token='15018490384' where id = '2181935';


-- 路福联合广场对接的秘钥 by 杨崇鑫
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('openapi.lufu.key', '70f2ea6d54fb44d5a18ac11f66d25154', '路福联合广场对接的秘钥', 999963, NULL);
