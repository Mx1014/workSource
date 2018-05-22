-- 考勤4.2 缺勤改成旷工
UPDATE eh_locale_strings SET TEXT ='旷工' WHERE scope ='punch.status' AND CODE = 3;