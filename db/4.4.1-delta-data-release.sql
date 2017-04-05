-- 2017-03-31 19:17 add by yanjun
insert into `eh_locale_strings` (`scope`, `code`, `locale`, `text`) values('activity','11','zh_CN','活动时间：');

-- 华润招租管理 add by sw 20170405
UPDATE eh_launch_pad_items SET action_type = 28, action_data = '' where item_label = '看房' and namespace_id = 999985;
UPDATE eh_lease_configs set renew_flag = 0 where id = 3;

