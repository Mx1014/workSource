package com.everhomes.yellowPage;

import org.jooq.Record;
import org.jooq.UpdateQuery;

public interface UpdateQueryBuilderCallback {
	 void buildCondition(UpdateQuery<? extends Record> query);
}
