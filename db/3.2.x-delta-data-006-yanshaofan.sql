update `eh_buildings` set namespace_id = 1000000 where id in (176121,176123,176124);

update `eh_yellow_pages` set id = 10000000 where name = '深圳仲裁委员会科技园工作站';
update `eh_yellow_pages` set id = 10005 where id = 5;
update `eh_yellow_pages` set id = 5 where id = 10000000;

