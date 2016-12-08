update eh_users set update_time = now();
update eh_organizations set update_time = now() where update_time is null;