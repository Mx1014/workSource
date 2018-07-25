-- rental3.6 by st.zheng
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'scene', 'zh_CN', '用户类型');

update `eh_rentalv2_cells` set `cell_id` = `id` where `cell_id`is null;