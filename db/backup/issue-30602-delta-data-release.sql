
-- 通用脚本
-- ADD BY 黄良铭  2018年7月3日
-- issue-30602  消息推送V2.2 iOS推送流程升级，少证书版本

SET @c_id = (SELECT IFNULL(MAX(id),1) FROM eh_developer_account_info);

INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.techpark.ios.zuolin','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:08:58','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.ios','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:11:41','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.dashahejiantou.ios.custom','AHS6J3P93Q','VSF7L946QT','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgxndN+H066MaLTMcVfReEH3h+zRJvzRGa1htf+ZS+GcCgCgYIKoZIzj0DAQehRANCAATMWieJA9Zg4MM/SIgQMqFavpdkl5TPHNQ3eE7Ac7gLj6qkvBY6PKUAZrRDFOWLd1Q5GlEMb++1eZMHbPpq6Tao','2018-06-13 16:56:30','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.cshidai.ios.custom','7U3CS4KE7L','K95KQU5W33','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZFBo9Zs1r0vCW1/GlXIWVInRD9YAV/3vOZPpgHRP4RCgCgYIKoZIzj0DAQehRANCAASHx9QZLr+ui9S3kHJn4mlJ+3Xs13lAE4pLYYa8ab0xYcO92iYcvII21wHVErUdljxmTrJmyLbRqEPxMVxsyEwM','2018-06-13 16:58:07','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.qidixiangshan.ios','CB8GBE78S6','579796H6SZ','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZkPKJv9d0476NRyK+/Lilnh9zlQIoQhTZpw6ib1i/rWgCgYIKoZIzj0DAQehRANCAATe4FEwlSalfQUXkuyGvewPaJexURCt3NvqILtR3yHpahPmcf3zNleyqxZtrx3p/oZslSGKG1TcAaT65RIIdHlC','2018-06-13 16:59:00','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.changzhihuiV1.ios','W2VC9SAD48','4YMXXWUW56','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgmjr+rWQF+GAZu7yeT7hd61h2xO2wbKWLa1hiLlOjF6CgCgYIKoZIzj0DAQehRANCAASHpSxhCQRuYv64cIY9qK7rMOJj6A69tzqWOlluzzTbYTgPHg3MnEwVEZ8JTY0zDKPh9tV0RM4IcALyt6izCuqP','2018-06-13 17:00:28','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xinchengzhihui.ios','EWGTG9634M','89R6W792SN','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQge3a6g0w1CyQbN550h3/U6eIOIizxm7I1CGcGY5Du/xqgCgYIKoZIzj0DAQehRANCAAQKGbiI5VFcgkMEzlzRvpH35XSjqH3dQfRRA0+d+XpO+qImBohENCFuAEPJ377OT6+riKJXIZOCjKsCAL0Ux5b0','2018-06-13 17:01:42','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xingshanghui.ios','Z94T2ZAC3Y','BM353BPZ8D','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgjzhpaksTeIFPcT9LKrMPB6KlprXqddL/TxnTwBh2YrSgCgYIKoZIzj0DAQehRANCAART9X8VuS9GVcdLFV4cIjHYHrqb1c/JvlK3D/1Po1XiWCCm6svVACIWC3EB6r3o1MTu8tNMmgCXLOKLN0flD04h','2018-06-13 17:02:32','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.baojiezhigu.ios.release','9DDTF2D6F9','V9KULR6LUW','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgBNdZytMskEl/El3NRTdJ0UWb0kbt09LuZ3rL7KLfWxOgCgYIKoZIzj0DAQehRANCAATkS3McOx8J+goHQ0hihPcskWgYi9mWSyDQ51AhYT3k+Oplc5LxMcRo+dilYLKzM6zCbMEfQE4aaMa6/FWjTU7Z','2018-06-13 17:03:46','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xinweilai.ios','RZW43KR9Q4','PU68B9P9QS','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg7BgXf3zlgxFj6hmPVB6wLrAe5glhNKBDTcdRzywJaySgCgYIKoZIzj0DAQehRANCAAQQzPNYG9+mAhxA7XAgyhkuK87Xp1bhconXboQ2KLG4Wo/oqNj1wstgLhRdDLmfiArWeZn2hBeb0XaqIqyxJO0i','2018-06-13 17:08:01','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.kexing.ios','424FEFTT88','Y3X3PV3ZN9','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZw/YZBFOt4/ngxEYTqUWDb5m4+NywDbpVoCOqJJ4+4CgCgYIKoZIzj0DAQehRANCAAQSUXcU1Hi+sHbgyOh3G4MGM2yftY77sNYe0n0GoM3gVs9b8StMnpsSWxQNDfsrw8B5EvvDcDHleY1fBzfeAhIq','2018-06-13 17:08:46','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.coastalpark.ios','GQ85Y2XDQ3','BDDR55F8B5','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg6N1jQ2A1u2d9Jyn0TDpDdIf47604bt2z03koge9fXCSgCgYIKoZIzj0DAQehRANCAAS6A6ACWWvTnt0dj38UaPq8MiNfL98kxX2vC4TOkYvPhnwNLtG6pR5yFV3NqDZnsZi8DnsPQ4Oasps33S+o3I5D','2018-06-13 17:10:50','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.oa.ios','T2777ZE59G','D8X4WHNV9A','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg8fthR/B8nnCWVRCwoNa2rHp1U4sM1QqGMQssVAbWjpugCgYIKoZIzj0DAQehRANCAARvoymYJ2M5uC3B5xWt82hA/NoOUAjJZeophmpt+5A3aAjb5O5vM806xceKxNMwo7K4Mkk1mkS+rfXvkDhhL/b1','2018-06-13 17:12:40','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.upark.ios.custom','T2777ZE59G','D8X4WHNV9A','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg8fthR/B8nnCWVRCwoNa2rHp1U4sM1QqGMQssVAbWjpugCgYIKoZIzj0DAQehRANCAARvoymYJ2M5uC3B5xWt82hA/NoOUAjJZeophmpt+5A3aAjb5O5vM806xceKxNMwo7K4Mkk1mkS+rfXvkDhhL/b1','2018-06-13 17:12:40','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.junminronghe.ios.custom','7R8J548669','N4Q33TX6FT','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgftOeRKfafY1+SW5GYmjZBYb1OYoqD42P20fss0+3Pd6gCgYIKoZIzj0DAQehRANCAATqbAYxO4w/03QzBnovtCOmiraoaRkbSdirJE/f61oybR7dIM4ZNgGutDrxseVrdO2FI1i27in8IVGcUN/DgG0S','2018-06-13 17:13:19','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.Dms365.GMQ','6TW7VRG635','5TUW2BDUUK','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgFtIkB8orcVPNvr5CR0g5GgsX+ubdoYU45DNzz6hoLPKgCgYIKoZIzj0DAQehRANCAASxSX6jYTPpbosIF/j35t26irE2LcDrha72WaaECdVVyCvi06bfhaV40l+vDtvcHg+e9B2IepAXLBpofIju/Y+2','2018-06-13 17:13:58','');


SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_bundleid_mapper);

INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','develop ','com.techpark.ios.zuolin');
INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','appbeta','com.techpark.ios.zuolin');
INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','appstore','com.techpark.ios.zuolin');

SET @a_id = (SELECT MAX(id) FROM eh_configurations);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
VALUES (@a_id:= @a_id +1, 'apple.pusher.flag', '0','苹果推送方式开关，值为1时为基于http2的新方式推送，其他值（一般选0）或空为旧方式推送',0,NULL);

-- END
-- 深圳湾
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.mybay.ios','2PRZ3336PJ','UT7B95V928','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgOj5WmRF9x4eO3CASZUYBKv56BTkf1ZyhJJPhGcSJxC2gCgYIKoZIzj0DAQehRANCAATX2Qw+DzB3IocGXaVDfxX17WJ8D9PT8jaj7rRwKeHDXS1IXDidOVxAnhxedwNcP9UKqLu0zpqUIGvCvyC83hU0','2018-06-13 17:11:34','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.ios','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:11:41','');
--end

-- 清华信息港
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.qinghua.ios','RZW43KR9Q4','PU68B9P9QS','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg7BgXf3zlgxFj6hmPVB6wLrAe5glhNKBDTcdRzywJaySgCgYIKoZIzj0DAQehRANCAAQQzPNYG9+mAhxA7XAgyhkuK87Xp1bhconXboQ2KLG4Wo/oqNj1wstgLhRdDLmfiArWeZn2hBeb0XaqIqyxJO0i','2018-06-13 17:08:01','');
NSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.ios','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:11:41','');
--end
