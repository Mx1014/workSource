// @formatter:off
package com.everhomes.sequence;

import org.jooq.DSLContext;


public interface SequenceQueryCallback {
    Long maxSequence(DSLContext dbContext);
}
