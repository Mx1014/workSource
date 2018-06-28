package com.everhomes.sequence;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.naming.NameMapper;
import com.everhomes.server.schema.tables.pojos.EhAclinkFirmware;
import com.everhomes.server.schema.tables.pojos.EhEvents;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.sharding.ShardingProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SequenceServiceImplTest extends CoreServerTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceImplTest.class);

    private static final Long MAX_ID = 500000000L;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Test
    public void syncSequence() {
        sequenceService.syncSequence();

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlows.class));
        assertTrue(id > MAX_ID);
        LOGGER.debug("already id EhFlows maxId: {}", id);

        id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEvents.class));
        assertTrue(id > MAX_ID);
        LOGGER.debug("empty id EhEvents maxId: {}", id);

        id = this.sequenceProvider.getNextSequence("usr");
        assertTrue(id > MAX_ID);
        LOGGER.debug("accountName: {}", id);


//        id = this.shardingProvider.allocShardableContentId(EhUsers.class).second();
//        assertTrue(id > MAX_ID);
//        LOGGER.debug("EhUsers maxId: {}", id);
    }

    public static void main(String[] args) {
        String table = "EhFlows";
        long bucketHash = calculateBucketHash(table);
        long bucketId = bucketHash >> 10;
        String redisMapKey = "seq" + ":" + bucketId;
        System.out.println(redisMapKey);
    }

    private static long calculateBucketHash(Object key) {
        boolean var1 = false;

        int hash;
        try {
            String keyString = key.toString();
            if (keyString.matches("^(-[1-9]|[1-9])\\d*")) {
                hash = Integer.parseInt(keyString);
            } else {
                hash = key.hashCode();
            }
        } catch (NumberFormatException var4) {
            LOGGER.error("Value of the sequence key that determines Redis bucket can not be parsed as integer", var4);
            hash = key.hashCode();
        }

        long bucketHash;
        if (hash >= 0) {
            bucketHash = (long)hash;
        } else {
            bucketHash = (long)hash + 4294967296L;
        }

        return bucketHash;
    }
}