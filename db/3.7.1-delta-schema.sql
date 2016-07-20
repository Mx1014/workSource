ALTER TABLE `eh_activities`
	ADD COLUMN `official_flag` TINYINT NULL DEFAULT '0' COMMENT 'whether it is an official activity, 0 not, 1 yes' AFTER `media_url`;
	
ALTER TABLE `eh_forum_posts`
	ADD COLUMN `official_flag` TINYINT NULL DEFAULT '0' COMMENT 'whether it is an official activity, 0 not, 1 yes' AFTER `end_time`;