set @organization_member_id = 2000000;

update `eh_organization_members` em set `integral_tag4` = (select `integral_tag1` from `eh_enterprise_contacts` where id = em.id - @organization_member_id);

