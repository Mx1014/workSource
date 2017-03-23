alter table `ehcore`.`eh_activities` 
   add column `all_date_flag` tinyint(4) DEFAULT '0' NULL COMMENT 'whether it is an all date activity, 0 not, 1 yes' after `end_time`,
   change `signup_flag` `signup_flag` tinyint(4) NULL , 
   change `official_flag` `official_flag` tinyint(4) default '0' NULL  comment 'whether it is an official activity, 0 not, 1 yes';