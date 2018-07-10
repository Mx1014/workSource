package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.yellowPage.AllianceTagDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceTagValsDao;
import com.everhomes.server.schema.tables.pojos.EhAllianceTagVals;

@Component
public class AllianceTagValProviderImpl implements AllianceTagValProvider {

	@Autowired
	DbProvider dbProvider;

	@Autowired
	SequenceProvider sequenceProvider;

	@Override
	public void deleteTagValByOwnerId(Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhAllianceTagVals tagVal = Tables.EH_ALLIANCE_TAG_VALS;
		context.delete(tagVal).where(tagVal.OWNER_ID.eq(ownerId)).execute();
	}

	@Override
	public void createTagVal(AllianceTagVal tagVal) {

		// 设置动态属性 如id，createTime
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAllianceTagVals.class));

		tagVal.setId(id);

		// 使用dao方法
		getAllianceTagValDao(AccessSpec.readWrite()).insert(tagVal);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAllianceTagVals.class, null);
	}

	private EhAllianceTagValsDao getAllianceTagValDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceTagValsDao(context.configuration());
	}

	@Override
	public List<AllianceTagVal> listTagValsByOwnerId(Long ownerId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhAllianceTagVals tagVal = Tables.EH_ALLIANCE_TAG_VALS;
		com.everhomes.server.schema.tables.EhAllianceTag tag = Tables.EH_ALLIANCE_TAG;
		
		
		List<AllianceTagVal> tagValList = new ArrayList<>(10);
		context.select()
			.from(tagVal)
			.leftOuterJoin(tag).on(tag.ID.eq(tagVal.TAG_ID))
			.where(tagVal.OWNER_ID.eq(ownerId)
					.and(tag.ID.isNotNull())
					.and(tag.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode())))
			.fetch().map(r -> {
				AllianceTagVal item= new AllianceTagVal();
				item.setId(r.getValue(tagVal.ID));
				item.setTagId(r.getValue(tagVal.TAG_ID));
				item.setTagParentId(r.getValue(tagVal.TAG_PARENT_ID));
				item.setOwnerId(r.getValue(tagVal.OWNER_ID));
				
				//设置tagDto
				AllianceTagDTO dto = new AllianceTagDTO();
				dto.setId(r.getValue(tag.ID));
				dto.setValue(r.getValue(tag.VALUE));
				dto.setParentId(r.getValue(tag.PARENT_ID));
				dto.setIsDefault(r.getValue(tag.IS_DEFAULT));
				dto.setDeleteFlag(r.getValue(tag.DELETE_FLAG));
				dto.setDefaultOrder(r.getValue(tag.DEFAULT_ORDER));
				item.setTagDto(dto);
				
				tagValList.add(item);
				return null;
			});
		
		if (tagValList.size() == 0) {
			return null;
		}
		
		return tagValList;
	}

}
