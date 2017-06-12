
-- 增加一般标签和热门标签的区分，原有的标签全部为热门标签  add  by yanjun 20170612
UPDATE `eh_hot_tags`  SET  `hot_flag` = 1;