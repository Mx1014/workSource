package com.everhomes.rentalv2;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhRentalv2ResourceOrdersRecord;

public class RentalResourceOrderRecordMapper implements
		RecordMapper<Record, EhRentalv2ResourceOrdersRecord> {

	@SuppressWarnings("unchecked")
	@Override
	public EhRentalv2ResourceOrdersRecord map(Record r) {
		EhRentalv2ResourceOrdersRecord o = new EhRentalv2ResourceOrdersRecord();
		o.setId(r.getValue((Field<Long>) r.field("id")));
		//TODO:
//		o.setOwnerId(r.getValue((Field<Long>) r
//				.field("owner_id")));
//		o.setOwnerType(r.getValue((Field<String>) r
//				.field("owner_type")));
//		o.setSiteType(r.getValue((Field<String>) r.field("site_type")));
		o.setRentalOrderId(r.getValue((Field<Long>) r.field("rental_order_id")));
		o.setRentalResourceRuleId(r.getValue((Field<Long>) r
				.field("rental_resource_rule_id")));
		o.setRentalCount(r.getValue((Field<Double>) r.field("rental_count")));
		o.setTotalMoney(r.getValue((Field<java.math.BigDecimal>) r.field("total_money")));
		o.setCreatorUid(r.getValue((Field<Long>) r.field("creator_uid")));
		o.setCreateTime(r.getValue((Field<Timestamp>) r.field("create_time")));
		o.setOperatorUid(r.getValue((Field<Long>) r.field("operator_uid")));
		o.setOperateTime(r.getValue((Field<Timestamp>) r.field("operate_time")));
		return o;
	}
}
