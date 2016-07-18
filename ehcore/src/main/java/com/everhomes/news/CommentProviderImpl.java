// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectConditionStep;
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
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class CommentProviderImpl implements CommentProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentProviderImpl.class);

	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	private DbProvider dbProvider;

	@Override
	@SuppressWarnings({ "unchecked" })
	public void createComment(Class<?> pojoClass, Comment comment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(pojoClass));
		comment.setId(id);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); // 为了使id与create_time同序，最好放一起set

		getReadWriteDao(pojoClass).insert(ConvertHelper.convert(comment, pojoClass));

		DaoHelper.publishDaoAction(DaoAction.CREATE, pojoClass, null);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void updateComment(Class<?> pojoClass, Comment comment) {
		assert (comment.getId() != null);

		getReadWriteDao(pojoClass).update(ConvertHelper.convert(comment, pojoClass));

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, comment.getId());
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void deleteComment(Class<?> pojoClass, Long id) {
		assert (id != null);

		getReadWriteDao(pojoClass).deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, id);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Comment findCommentById(Class<?> pojoClass, Long id) {
		assert (id != null);

		return ConvertHelper.convert(getReadOnlyDao(pojoClass).findById(id), Comment.class);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Comment> listCommentByOwnerId(Class<?> pojoClass, Long ownerId) {
		assert (ownerId != null);

		DSLContext context = getReadOnlyContext();

		JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
		assert (meta != null);

		Record blankRecord = meta.getBlankRecordObject();
		assert (blankRecord != null);
		// 下面where的写法与 where("owner_id = ?", ownerId)是一样的
		return context.select().from(meta.getTableName())
				.where(((Field<Long>) blankRecord.field("owner_id")).eq(ownerId)).fetch()
				.map(new MyCommentRecordMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> listCommentByOwnerIdWithPage(Class<?> pojoClass, Long ownerId, Long pageAnchor,
			Integer pageSize) {
		assert (ownerId != null);

		DSLContext context = getReadOnlyContext();

		JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
		assert (meta != null);

		Record blankRecord = meta.getBlankRecordObject();
		assert (blankRecord != null);
		// 下面where的写法与 where("owner_id = ?", ownerId)是一样的
		SelectConditionStep<Record> selectConditionStep = context.select().from(meta.getTableName())
				.where(((Field<Long>) blankRecord.field("owner_id")).eq(ownerId))
				.and(((Field<Byte>) blankRecord.field("status")).eq(CommentStatus.ACTIVE.getCode()));
		if (pageAnchor.longValue() != 0L) {
			selectConditionStep.and(((Field<Long>) blankRecord.field("id")).lt(pageAnchor));
		}
		return selectConditionStep.orderBy(((Field<Timestamp>) blankRecord.field("create_time")).desc(),((Field<Long>) blankRecord.field("id")).desc())
				.limit(pageSize.intValue()).fetch().map(new MyCommentRecordMapper());
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

	private class MyCommentRecordMapper implements RecordMapper<Record, Comment> {

		@Override
		public Comment map(Record r) {
			Comment comment = new Comment();
			comment.setId(r.getValue("id", Long.class));
			comment.setOwnerId(r.getValue("owner_id", Long.class));
			comment.setContentType(r.getValue("content_type", String.class));
			comment.setContent(r.getValue("content", String.class));
			comment.setStatus(r.getValue("status", Byte.class));
			comment.setCreatorUid(r.getValue("creator_uid", Long.class));
			comment.setDeleterUid(r.getValue("deleter_uid", Long.class));
			comment.setCreateTime(r.getValue("create_time", Timestamp.class));
			comment.setDeleteTime(r.getValue("delete_time", Timestamp.class));
			return comment;
		}

	}
}
