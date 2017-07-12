--
-- 删除深业门牌里的家庭及家庭里的成员信息		add by xq.tian	2016/11/08
--
DELETE FROM eh_user_groups WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
    SELECT id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_group_members WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
    SELECT id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
  SELECT id FROM eh_addresses WHERE namespace_id = '999992'
);

UPDATE eh_organization_address_mappings SET living_status = '0' WHERE address_id IN (
  SELECT id FROM eh_addresses WHERE namespace_id = '999992'
);

--
-- 删除深业的客户资料信息		add by xq.tian	2016/11/08
--
DELETE FROM eh_organization_owner_address WHERE organization_owner_id IN (
  SELECT id FROM eh_organization_owners WHERE namespace_id = '999992' AND community_id IN (
    SELECT community_id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_organization_owners WHERE namespace_id = '999992' AND community_id IN (
  SELECT community_id FROM eh_addresses WHERE namespace_id = '999992'
);

DELETE FROM eh_organization_owner_behaviors WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_attachments WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_cars WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_car_attachments WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_owner_car WHERE namespace_id = '999992';