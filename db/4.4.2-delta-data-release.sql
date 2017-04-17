
-- 更改华润的查询方式为当前小区或者是管理公司，具体实现是scope使用3  add by yanjun 20170114

UPDATE eh_launch_pad_items SET action_data = REPLACE(action_data, '"scope":2', '"scope":3') WHERE namespace_id = 999985 AND action_type = 61 AND scene_type = 'park_tourist' ;
