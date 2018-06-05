update eh_configurations set value ='http://10.1.110.43/pay' where name='pay.v2.home.url';

 INSERT INTO `eh_namespace_pay_mappings`(`id`,`namespace_id`, `namespace_name`, `app_key`, `secret_key`) VALUES (2, 0, 'DefaultAccount',
'1dbf2b28-0613-4995-b030-f5316323c775','tHpsbDqtKWW7O2esa8eYkYZBdxS6Gk283u3otsQoEnCzKWUAK0zFBmkDyYkVSscTjKX+nOfuH31udJje/60XoA==');

INSERT INTO `eh_namespace_pay_mappings`(`id`,`namespace_id`, `namespace_name`, `app_key`, `secret_key`) VALUES (1,999992, 'TestAccount',
'22222222-0613-4995-b030-f5316323c775','2yF1jC61cGgvWQUoAsxiKPKZZQzFZ29O/57GKCis2vrM8taw1SL0Q2JGA/mI1toCzuZCOVDPMzewspzfYFYySg==');