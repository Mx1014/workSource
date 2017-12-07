-- 默认开启企业详情开关 by xiongying20171207

DROP PROCEDURE if exists create_community_organization_detail_display;
delimiter //
CREATE PROCEDURE `create_community_organization_detail_display` ()
BEGIN  
  DECLARE ns INTEGER;
  DECLARE communityid LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id, namespace_id from eh_communities where namespace_id > 0; 
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO communityid, ns;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        SET @display_id = (SELECT MAX(id) FROM `eh_community_organization_detail_display`);
        INSERT INTO eh_community_organization_detail_display (`id`, `namespace_id`, `community_id`, `detail_flag`, `operator_uid`, `update_time`)
            VALUES((@display_id := @display_id + 1), ns, communityid, 1, 1, NOW());
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL create_community_organization_detail_display;
DROP PROCEDURE if exists create_community_organization_detail_display;
