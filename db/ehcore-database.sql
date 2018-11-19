SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI';

USE `mysql`;

DROP PROCEDURE IF EXISTS `mysql`.`drop_user_if_exists` ;
DELIMITER $$
CREATE PROCEDURE `mysql`.`drop_user_if_exists`()
BEGIN
  DECLARE foo BIGINT DEFAULT 0 ;
  SELECT COUNT(*)
  INTO foo
    FROM `mysql`.`user`
      WHERE `User` = 'ehcore' and host = 'localhost';
  
  IF foo > 0 THEN 
         DROP USER 'ehcore'@'localhost' ;
  END IF;
  
  SELECT COUNT(*)
  INTO foo
    FROM `mysql`.`user`
      WHERE `User` = 'ehcore' and host = '%';
  
  IF foo > 0 THEN 
         DROP USER 'ehcore'@'%' ;
  END IF;
END $$
DELIMITER ;

CALL `mysql`.`drop_user_if_exists`();

DROP PROCEDURE IF EXISTS `mysql`.`drop_users_if_exists`;

SET SQL_MODE=@OLD_SQL_MODE ;

DROP DATABASE IF EXISTS `ehcore`;

CREATE DATABASE `ehcore`;

CREATE USER ehcore identified by 'ehcore';

GRANT ALL ON ehcore.* to ehcore@`localhost` identified by 'ehcore';
GRANT ALL ON ehcore.* to ehcore@`%` identified by 'ehcore';

GRANT process ON *.* TO ehcore@`localhost`;
GRANT process ON *.* TO ehcore@`%`;

COMMIT;
