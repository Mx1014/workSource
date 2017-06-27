set @id = 0;
insert into eh_rentalv2_price_rules 
(
    `id`,
    `owner_type`,
    `owner_id`,
    `rental_type`,
    `workday_price`,
    `weekend_price`,
    `org_member_workday_price`,
    `org_member_weekend_price`,
    `approving_user_workday_price`,
    `approving_user_weekend_price`, 
    `discount_type`,
    `full_price`,
    `cut_price`,
    `discount_ratio`,
    `creator_uid`,
    `create_time`
)
select @id:=@id+1,
         'default',
         t.id,
         t.rental_type,
         t.workday_price,
         t.weekend_price,
         t.org_member_workday_price,
         t.org_member_weekend_price,
         t.approving_user_workday_price,
         t.approving_user_weekend_price,
         null,
         null,
         null,
         null,
         0,
         now()
from eh_rentalv2_default_rules t
where t.rental_type is not null
union all
select @id:=@id+1,
         'resource',
         t2.id,
         t2.rental_type,
         t2.workday_price,
         t2.weekend_price,
         t2.org_member_workday_price,
         t2.org_member_weekend_price,
         t2.approving_user_workday_price,
         t2.approving_user_weekend_price,
         t2.discount_type,
         t2.full_price,
         t2.cut_price,
         t2.discount_ratio,
         0,
         now()
from eh_rentalv2_resources t2
where t2.rental_type is not null;


