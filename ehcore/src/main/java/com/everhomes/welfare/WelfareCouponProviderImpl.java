// @formatter:off
package com.everhomes.welfare;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWelfareCouponsDao;
import com.everhomes.server.schema.tables.pojos.EhWelfareCoupons;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class WelfareCouponProviderImpl implements WelfareCouponProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWelfareCoupon(WelfareCoupon welfareCoupon) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWelfareCoupons.class));
		welfareCoupon.setId(id);
		welfareCoupon.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(welfareCoupon);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWelfareCoupons.class, null);
	}

	@Override
	public void updateWelfareCoupon(WelfareCoupon welfareCoupon) {
		assert (welfareCoupon.getId() != null);
		getReadWriteDao().update(welfareCoupon);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfareCoupons.class, welfareCoupon.getId());
	}

	@Override
	public WelfareCoupon findWelfareCouponById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), WelfareCoupon.class);
	}
	
	@Override
	public List<WelfareCoupon> listWelfareCoupon() {
		return getReadOnlyContext().select().from(Tables.EH_WELFARE_COUPONS)
				.orderBy(Tables.EH_WELFARE_COUPONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, WelfareCoupon.class));
	}
	
	private EhWelfareCouponsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWelfareCouponsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWelfareCouponsDao getDao(DSLContext context) {
		return new EhWelfareCouponsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
