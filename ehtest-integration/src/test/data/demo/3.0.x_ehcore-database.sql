DROP DATABASE IF EXISTS `ehcore_test`;
CREATE DATABASE `ehcore_test`;

GRANT ALL ON ehcore_test.* to ehcore_test@`localhost` identified by 'ehcore_test';
GRANT ALL ON ehcore_test.* to ehcore_test@`%` identified by 'ehcore_test';

GRANT process ON *.* TO ehcore_test@`localhost`;
GRANT process ON *.* TO ehcore_test@`%`;

flush privileges;

COMMIT;
