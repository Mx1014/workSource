-- by zheng 初始化communityId
update `eh_lease_promotions`  right join `eh_lease_buildings` on `eh_lease_promotions`.building_id = `eh_lease_buildings`.id
set `eh_lease_promotions`.community_id = `eh_lease_buildings`.community_id
where `eh_lease_promotions`.building_id>0;