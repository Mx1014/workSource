package com.everhomes.techpark.rental;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Field;

import com.everhomes.server.schema.tables.records.EhRentalSitesBillsRecord;

public class RentalSitesBillRecordMapper implements
		RecordMapper<Record, EhRentalSitesBillsRecord> {

	@SuppressWarnings("unchecked")
	@Override
	public EhRentalSitesBillsRecord map(Record r) {
		EhRentalSitesBillsRecord o = new EhRentalSitesBillsRecord();
		o.setId(r.getValue((Field<Long>) r.field("id")));
		//TODO:
//		o.setOwnerId(r.getValue((Field<Long>) r
//				.field("owner_id")));
//		o.setOwnerType(r.getValue((Field<String>) r
//				.field("owner_type")));
//		o.setSiteType(r.getValue((Field<String>) r.field("site_type")));
		o.setRentalBillId(r.getValue((Field<Long>) r.field("rental_bill_id")));
		o.setRentalSiteRuleId(r.getValue((Field<Long>) r
				.field("rental_site_rule_id")));
		o.setRentalCount(r.getValue((Field<Double>) r.field("rental_count")));
		o.setTotalMoney(r.getValue((Field<java.math.BigDecimal>) r.field("total_money")));
		o.setCreatorUid(r.getValue((Field<Long>) r.field("creator_uid")));
		o.setCreateTime(r.getValue((Field<Timestamp>) r.field("create_time")));
		o.setOperatorUid(r.getValue((Field<Long>) r.field("operator_uid")));
		o.setOperateTime(r.getValue((Field<Timestamp>) r.field("operate_time")));
		return o;
	}
}
