-- rental3.5 by st.zheng
update eh_locale_strings set text = '用户姓名' where scope = 'rental.flow' and `code` = 'user';
update eh_locale_strings set text = '联系方式' where scope = 'rental.flow' and `code` = 'contact';
update eh_locale_strings set text = '公司名称' where scope = 'rental.flow' and `code` = 'organization';
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'spec', 'zh_CN', '资源规格');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'address', 'zh_CN', '资源地址');
update eh_locale_strings set text = '免费物资' where scope = 'rental.flow' and `code` = 'goodItem';
update eh_locale_strings set text = '付费商品' where scope = 'rental.flow' and `code` = 'item';
