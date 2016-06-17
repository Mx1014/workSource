package com.everhomes.hotTag;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.hotTag.HotTagStatus;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandards;
import com.everhomes.util.DateHelper;

@Component
public class HotTagProviderImpl implements HotTagProvider {
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private DbProvider dbProvider;

	@Override
	public List<TagDTO> listHotTag(String serviceType, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateHotTag(HotTags tag) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createHotTag(HotTags tag) {
		// TODO Auto-generated method stub
		
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionStandards.class));
		
		tag.setId(id);
		tag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		tag.setStatus(HotTagStatus.ACTIVE.getCode());
	}

	@Override
	public HotTags findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
