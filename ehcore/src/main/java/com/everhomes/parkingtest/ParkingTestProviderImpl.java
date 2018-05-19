package com.everhomes.parkingtest;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.util.ConvertHelper;

/**
 * 
 * @author dingjianmin
 *dao层，开始操作数据库
 */
@Component
public class ParkingTestProviderImpl implements ParkingTestProvider {

	@Override
	public List<ParkingLotTest> listParkingLotsTest(Long Id, int from, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParkingLotTest findParkingLotById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateParkingLotTest(ParkingLotTest parkingLot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createParkingSpace(ParkingLotTest parkingLot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ParkingLotTest findParkingCarVerificationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void DeleteListParkingLotsTest(ParkingLotTest verification) {
		// TODO Auto-generated method stub
		
	}/*

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingTestProviderImpl.class);

	@Autowired
    private SequenceProvider sequenceProvider;
	
	@Autowired
    private DbProvider dbProvider;
    
 
	//@Cacheable(value="listParkingLotsTestSelectCache",unless="#result.size() == 0")
    @Override
    public List<ParkingLotTest> listParkingLotsTest(Long Id,int from, int pageSize) {
    	
    	//获取EhParkingLots上的连接，操作该张表
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        
        //查询，参数返回的类型是pooj生产的
        
        SelectQuery<EhTestInfosRecord> query = context.selectQuery(Tables.EH_TEST_INFOS);
        
        //添加查询条件
        if(null != Id)
        	query.addConditions(Tables.EH_TEST_INFOS.ID.eq(Id));
        
        
        if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_TEST_INFOS.ID.lt(pageAnchor));
        
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        
        
        System.out.println("查询的SQL:"+query.getSQL());
        
        //这些不太懂
        return query.fetch().map(r -> {
        	ParkingLotTest parkingLot = ConvertHelper.convert(r, ParkingLotTest.class);
			//populateParkingConfigInfo(parkingLot);
			return parkingLot;
		});
        
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_TEST_INFOS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_TEST_INFOS.AGE.asc())
				.limit(from, pageSize)
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, ParkingLotTest.class));
		}
		
		return new ArrayList<ParkingLotTest>();
    }

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}
	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}

	@Override
	public ParkingLotTest findParkingLotById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        EhTestInfosDao dao = new EhTestInfosDao(context.configuration());

        ParkingLotTest parkingLot = ConvertHelper.convert(dao.findById(id), ParkingLotTest.class);

		//populateParkingConfigInfo(parkingLot);

        return parkingLot;
	}

	@Override
	public void updateParkingLotTest(ParkingLotTest parkingLot) {
			DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
			EhTestInfosDao dao = new EhTestInfosDao(context.configuration());
			dao.update(parkingLot);
	        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTestInfos.class, parkingLot.getId());
	        
	        
	        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
	        
	        int count = context.update(Tables.EH_TEST_INFOS).set(Tables.EH_TEST_INFOS.NAME, parkingLot.getName())
	                .set(Tables.EH_TEST_INFOS.AGE, parkingLot.getAge())
	                .where(Tables.EH_TEST_INFOS.ID.in(parkingLot.getId())).execute();

	        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, parkingLot.getId());
	        

	}


	//EhParkingSpaces
	@Override
	public void createParkingSpace(ParkingLotTest parkingLot) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhTestInfos.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhTestInfosDao dao = new EhTestInfosDao(context.configuration());

		parkingLot.setId(id);
		parkingLot.setCreateTime(new Timestamp(System.currentTimeMillis()));
		parkingLot.setCreatorUid(UserContext.currentUserId());

		dao.insert(parkingLot);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTestInfos.class, id);
		
	}


	@Override
	public ParkingLotTest findParkingCarVerificationById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        EhTestInfosDao dao = new EhTestInfosDao(context.configuration());

		ParkingLotTest parkingLot = ConvertHelper.convert(dao.findById(id), ParkingLotTest.class);

		//populateParkingConfigInfo(parkingLot);

        return parkingLot;
	}


	@Caching(evict = { @CacheEvict(value="listParkingLotsTestSelectCache", allEntries=true)})
	@Override
	public void DeleteListParkingLotsTest(ParkingLotTest verification) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhTestInfosDao dao = new EhTestInfosDao(context.configuration());

		//dao.update(verification);
		
		dao.delete(verification);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTestInfos.class, null);
	}
	*/

}
