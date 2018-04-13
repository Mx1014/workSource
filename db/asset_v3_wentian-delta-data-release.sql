set @eh_locale_strings_id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1001', 'zh_CN', '客户类型错误，只允许填写个人客户或者企业客户');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1002', 'zh_CN', '日期格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1003', 'zh_CN', '客户名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1004', 'zh_CN', '催缴手机号不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1005', 'zh_CN', '收费项名称错误，请确认是否在管理后台已经更改了收费项名称，建议重新下载模板');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'assetBillImport', '1006', 'zh_CN', '金额格式错误，应该为0.00');

