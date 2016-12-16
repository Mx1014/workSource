update eh_users set update_time = now() where update_time is null;
update eh_organizations set update_time = now() where update_time is null;
update eh_organization_address_mappings set create_time = now(), update_time = now() where update_time is null;
update eh_rentalv2_orders set operate_time = now() where operate_time is null;
update eh_activities set update_time = now() where update_time is null;
update eh_office_cubicle_orders set create_time = now(), update_time = now() where update_time is null;
