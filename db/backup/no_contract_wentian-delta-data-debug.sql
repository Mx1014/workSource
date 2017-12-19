SET @local_id = (SELECT MAX(`id`) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@local_id:=@local_id+1, 'assetv2', '10007', 'zh_CN', '存在已支付的账单，请刷新后再尝试支付');
