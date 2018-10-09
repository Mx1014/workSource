-- AUTHOR: 丁建民
-- REMARK: #31812   保存用户当前所在场景

SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `ehcore`.`eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20017', 'zh_CN', '周期输入格式不正确');
INSERT INTO `ehcore`.`eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20018', 'zh_CN', '授权价格式不正确');

INSERT INTO `ehcore`.`eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10013', 'zh_CN', '合同计价条款计费周期不存在');
INSERT INTO `ehcore`.`eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10014', 'zh_CN', '该房源信息已经存在');
-- END
