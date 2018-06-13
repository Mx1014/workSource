-- organization owner  by jiarui 
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18002', 'zh_CN', '必填项未填写');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18003', 'zh_CN', '楼栋门牌不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18004', 'zh_CN', '时间格式不正确');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18005', 'zh_CN', '入驻状态格式错误');

-- apartment owner  by dingjianmin
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'address', '20011', 'zh_CN', '门牌状态不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'address', '20012', 'zh_CN', '门牌朝向输入过长');

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10013', 'zh_CN', '项目名称不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10014', 'zh_CN', '项目地址超过指定长度');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10015', 'zh_CN', '省市区不能为空');
