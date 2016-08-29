// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqDiscover;
import com.everhomes.jooq.JooqMetaInfo;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AttachmentProviderImpl implements AttachmentProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@SuppressWarnings("unchecked")
	public void createAttachment(Class<?> pojoClass, Attachment attachment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(pojoClass));
		attachment.setId(id);
		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		getReadWriteDao(pojoClass).insert(ConvertHelper.convert(attachment, pojoClass));

		DaoHelper.publishDaoAction(DaoAction.CREATE, pojoClass, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void createAttachments(Class<?> pojoClass, List<Attachment> attachments) {
	    List<Object> list = attachments.stream().map(attachment -> {
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(pojoClass));
			attachment.setId(id);
			attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			// 必须强制转换，否则List<R>转不成List<Object> by lqs 20160623
			return (Object)ConvertHelper.convert(attachment, pojoClass);
		}).collect(Collectors.toList());
	    
		getReadWriteDao(pojoClass).insert(list);

		DaoHelper.publishDaoAction(DaoAction.CREATE, pojoClass, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateAttachment(Class<?> pojoClass, Attachment attachment) {
		assert (attachment.getId() != null);

		getReadWriteDao(pojoClass).update(attachment);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, attachment.getId());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deleteAttachment(Class<?> pojoClass, Long id) {
		assert (id != null);

		getReadWriteDao(pojoClass).deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Attachment findAttachmentById(Class<?> pojoClass, Long id) {
		assert (id != null);

		return ConvertHelper.convert(getReadOnlyDao(pojoClass).findById(id), Attachment.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Attachment> listAttachmentByOwnerId(Class<?> pojoClass, Long ownerId) {
		assert (ownerId != null);

		DSLContext context = getReadOnlyContext();

		JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
		assert (meta != null);

		Record blankRecord = meta.getBlankRecordObject();
		assert (blankRecord != null);
		// 下面where的写法与 where("owner_id = ?", ownerId)是一样的
		return context.select().from(meta.getTableName())
				.where(((Field<Long>) blankRecord.field("owner_id")).eq(ownerId)).fetch()
				.map(new MyAttachmentRecordMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Attachment> listAttachmentByOwnerIds(Class<?> pojoClass, List<Long> ownerIds) {
		assert (ownerIds != null && ownerIds.size() > 0);

		JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
		assert (meta != null);

		Record blankRecord = meta.getBlankRecordObject();
		assert (blankRecord != null);

		return (List<Attachment>) getReadOnlyDao(pojoClass)
				.fetch((Field<Long>) blankRecord.field("owner_id"), ownerIds).stream()
				.map(p -> ConvertHelper.convert(p, Attachment.class)).collect(Collectors.toList());
	}

	@SuppressWarnings("rawtypes")
	private DAOImpl getDao(Class<?> pojoClass, DSLContext context) {
		try {
			JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
			assert (meta != null);

			DAOImpl dao = (DAOImpl) meta.getDaoClass().newInstance();
			dao.setConfiguration(context.configuration());

			return dao;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Unexpected exception", e);
			throw new RuntimeException("Unexpected exception when constructing DAO instance for POJO " + pojoClass);
		}
	}

	@SuppressWarnings("rawtypes")
	private DAOImpl getReadWriteDao(Class<?> pojoClass) {
		return getDao(pojoClass, getReadWriteContext());
	}

	@SuppressWarnings("rawtypes")
	private DAOImpl getReadOnlyDao(Class<?> pojoClass) {
		return getDao(pojoClass, getReadOnlyContext());
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

	private class MyAttachmentRecordMapper implements RecordMapper<Record, Attachment> {

		@Override
		public Attachment map(Record r) {
			Attachment attachment = new Attachment();
			attachment.setId(r.getValue("id", Long.class));
			attachment.setOwnerId(r.getValue("owner_id", Long.class));
			attachment.setContentType(r.getValue("attachment_name", String.class));
			attachment.setContentType(r.getValue("content_type", String.class));
			attachment.setContentUri(r.getValue("content_uri", String.class));
			attachment.setCreatorUid(r.getValue("creator_uid", Long.class));
			attachment.setCreateTime(r.getValue("create_time", Timestamp.class));
			return attachment;
		}

	}
}
