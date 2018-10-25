package com.everhomes.yellowPage.faq;

import org.jooq.Record;
import org.jooq.UpdateQuery;

public interface UpdateQueryBuilderCallback {
	 void buildCondition(UpdateQuery<? extends Record> query);
}
