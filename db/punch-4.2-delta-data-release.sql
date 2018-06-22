-- 考勤4.2 
-- 缺勤改成旷工
UPDATE eh_locale_strings SET TEXT ='旷工' WHERE scope ='punch.status' AND CODE = 3;
-- 重新刷考勤月报数据
DELETE FROM eh_punch_month_reports;
SET @id := 1;
INSERT INTO eh_punch_month_reports(id, punch_month,owner_type,owner_id, STATUS,PROCESS,creator_uid,create_time , update_time ,punch_member_number )  
  SELECT   (@id :=@id +1) id, punch_month,owner_type,owner_id, 1 STATUS,100 PROCESS,creator_uid,MAX(create_time),MAX(create_time) ,COUNT(1) punch_member_number FROM eh_punch_statistics
GROUP BY punch_month,owner_type,owner_id ORDER BY punch_month DESC   ;
-- 考勤4.2  end