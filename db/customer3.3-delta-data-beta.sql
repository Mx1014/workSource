
-- 先ES所在serevr执行下 curl -XDELETE localhost:9200/everhomesv3/enterpriseCustomer
-- 1: 迁移organization to customer
-- /customer/syncOrganizationToCustomer


-- 2:同步下客户管理ES
-- /customer/syncEnterpriseCustomerIndex


--  3:同步下企业管理ES
--  /org/syncIndex