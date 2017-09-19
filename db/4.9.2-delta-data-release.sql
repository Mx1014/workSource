-- 修复detail的数据 by lei.lv
UPDATE eh_organization_member_details md
INNER JOIN (
	SELECT
		m.namespace_id,
		m.detail_id
	FROM
		eh_organization_members m
	INNER JOIN eh_organization_member_details d ON d.id = m.detail_id
	AND m.`status` = '3'
	AND m.namespace_id != '' AND m.organization_id = d.organization_id
) AS t1 ON t1.detail_id = md.id
SET md.namespace_id = t1.namespace_id;