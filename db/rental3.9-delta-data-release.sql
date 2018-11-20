SET @id = (SELECT IFNULL(MIN(id),0) from `eh_vip_priority`);
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,1,'银卡',10 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,2,'金卡',20 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,3,'白金卡',30 );

set @classification_id = 0;
-- 普通公司用户价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'enterprise',workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type from eh_rentalv2_price_rules;
-- 管理公司员工价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'pm_admin',org_member_workday_price,org_member_original_price,org_member_initiate_price,org_member_discount_type,org_member_full_price,org_member_cut_price,org_member_discount_ratio,resource_type from eh_rentalv2_price_rules;
-- 非认证用户价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
 SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'park_tourist',approving_user_workday_price,approving_user_original_price,approving_user_initiate_price,approving_user_discount_type,approving_user_full_price,approving_user_cut_price,approving_user_discount_ratio,resource_type from eh_rentalv2_price_rules ;
-- 单元格价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'enterprise',price,original_price,initiate_price,resource_type from eh_rentalv2_cells ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'pm_admin',org_member_price,org_member_original_price,org_member_initiate_price,resource_type from eh_rentalv2_cells ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'park_tourist',approving_user_price,approving_user_original_price,approving_user_initiate_price,resource_type from eh_rentalv2_cells ;

-- 套餐价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'enterprise',price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type  from eh_rentalv2_price_packages where owner_type != 'cell' ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'pm_admin',org_member_price,org_member_original_price,org_member_initiate_price,org_member_discount_type,org_member_full_price,org_member_cut_price,org_member_discount_ratio,resource_type from eh_rentalv2_price_packages where owner_type != 'cell' ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'park_tourist',approving_user_price,approving_user_original_price,approving_user_initiate_price,approving_user_discount_type,approving_user_full_price,approving_user_cut_price,approving_user_discount_ratio,resource_type from eh_rentalv2_price_packages where owner_type != 'cell' ;