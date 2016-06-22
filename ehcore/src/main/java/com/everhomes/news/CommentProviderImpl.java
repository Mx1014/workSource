// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
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

@Component
public class CommentProviderImpl implements CommentProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentProviderImpl.class);

	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	private DbProvider dbProvider;

	@SuppressWarnings("rawtypes")
	private DAOImpl getDao(Class<?> pojoClass, AccessSpec accessSpec) {
		try {
			DSLContext context = dbProvider.getDslContext(accessSpec);

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
		return getDao(pojoClass, AccessSpec.readWrite());
	}

	@SuppressWarnings("rawtypes")
	private DAOImpl getReadOnlyDao(Class<?> pojoClass) {
		return getDao(pojoClass, AccessSpec.readOnly());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createComment(Class<?> pojoClass, Comment comment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(pojoClass));
		comment.setId(id);

		DAOImpl dao = getReadWriteDao(pojoClass);
		dao.insert(ConvertHelper.convert(comment, pojoClass));

		DaoHelper.publishDaoAction(DaoAction.CREATE, pojoClass, null);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateComment(Class<?> pojoClass, Comment comment) {
		assert (comment.getId() != null);

		DAOImpl dao = getReadWriteDao(pojoClass);
		dao.update(ConvertHelper.convert(comment, pojoClass));

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, comment.getId());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteComment(Class<?> pojoClass, Long id) {
		assert (id != null);

		DAOImpl dao = getReadWriteDao(pojoClass);
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, pojoClass, id);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Comment findCommentById(Class<?> pojoClass, Long id) {
		assert (id != null);

		DAOImpl dao = getReadOnlyDao(pojoClass);
		return ConvertHelper.convert(dao.findById(id), Comment.class);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Comment> listComment(Class<?> pojoClass, Long ownerId) {
		assert (ownerId != null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(pojoClass);
		assert (meta != null);

		Record blankRecord = meta.getBlankRecordObject();
		assert (blankRecord != null);
		// 下面where的写法与 where("owner_id = ?", ownerId)是一样的
		return context.select().from(meta.getTableName())
				.where(((Field<Long>) blankRecord.field("owner_id")).eq(ownerId)).fetch().map(r -> {
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
				});
	}
}
