SET @id = (SELECT IFNULL(MIN(id),0) from `eh_vip_priority`);
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,1,'银卡',10 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,2,'金卡',20 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,3,'白金卡',30 );