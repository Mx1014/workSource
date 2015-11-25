package com.everhomes.techpark.onlinePay;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRechargeInfoDao;
import com.everhomes.server.schema.tables.pojos.EhRechargeInfo;
import com.everhomes.techpark.onlinePay.OnlinePayProvider;
import com.everhomes.techpark.park.RechargeInfo;
import com.everhomes.util.ConvertHelper;

@Component
public class OnlinePayProviderImpl implements OnlinePayProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Override
	public RechargeInfo findRechargeInfoByOrderId(Long orderId) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select().from(Tables.EH_RECHARGE_INFO)
				.where(Tables.EH_RECHARGE_INFO.BILL_ID.eq(orderId))
				.fetchOne();
		if(record != null)
			return ConvertHelper.convert(record, RechargeInfo.class);
		return null;
	}

	@Override
	public void updateRehargeInfo(RechargeInfo rechargeOrder) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhRechargeInfoDao dao = new EhRechargeInfoDao(context.configuration());
		dao.update(ConvertHelper.convert(rechargeOrder, EhRechargeInfo.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRechargeInfo.class, rechargeOrder.getId());
	}

}
