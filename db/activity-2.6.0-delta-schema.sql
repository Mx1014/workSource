alter table `ehcore`.`eh_activities` 
   add column `all_day_flag` tinyint(4) DEFAULT '0' NULL COMMENT 'whether it is an all day activity, 0 not, 1 yes' after `end_time`;