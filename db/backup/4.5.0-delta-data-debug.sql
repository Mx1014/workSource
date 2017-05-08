-- 社群参数修改 add by sfyan 20170504
update `eh_launch_pad_items` set `action_data` = '{\"url\":\"zl://association/main?layoutName=AssociationLayout&itemLocation=/association&versionCode=2017042501&displayName=社群\"}' where `item_name` = 'Association' and `item_label` = '社群' and namespace_id = 999985;
