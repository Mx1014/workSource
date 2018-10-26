package com.everhomes.sequence;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.records.EhSequencesRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.TableLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class TestSequenceProvider implements SequenceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceProviderImpl.class);
    @Autowired
    private BigCollectionProvider collectionProvider;
    @Autowired
    private DbProvider dbProvider;
    private Map<String, Long> domainStartSequences = new HashMap();

    public TestSequenceProvider() {
    }

    @PostConstruct
    private void setup() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<EhSequencesRecord> recs = context.select(new Field[0]).from(new TableLike[]{Tables.EH_SEQUENCES}).fetch().into(EhSequencesRecord.class);
        Iterator var3 = recs.iterator();

        while(var3.hasNext()) {
            EhSequencesRecord r = (EhSequencesRecord)var3.next();
            this.domainStartSequences.put(r.getDomain(), r.getStartSeq());
        }

    }

    public long getNextSequence(String sequenceDomain) {
        Accessor accessor = this.collectionProvider.getMapAccessor("seq", sequenceDomain);
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        long sequence = 2L;

        try {
            Object val = template.opsForHash().get(accessor.getBucketName(), sequenceDomain);
            if (val == null || val.toString().isEmpty() || !val.toString().matches("\\d+")) {
                LOGGER.warn("Invalid sequence-generator value: {} found for sequence domain: {}", val != null ? val.toString() : "null", sequenceDomain);
            }

            sequence = template.opsForHash().increment(accessor.getBucketName(), sequenceDomain, 1L);
            if (sequence == 1L) {
                sequence = template.opsForHash().increment(accessor.getBucketName(), sequenceDomain, 1L);
            }
        } catch (Exception var7) {
            LOGGER.error("Unexpected exception while getting sequence from Redis", var7);
            throw new RuntimeException("Unexpected exception while getting sequence from Redis", var7);
        }

        Long startSequence = (Long)this.domainStartSequences.get(sequenceDomain);
        if (startSequence != null && sequence < startSequence) {
            LOGGER.debug("next sequence {} for sequence domain {} is smaller than configured start sequence, adjust it", sequence - 1L, sequenceDomain);
            sequence = template.opsForHash().increment(accessor.getBucketName(), sequenceDomain, startSequence);
        }

        LOGGER.debug("Return next sequence {} for sequence domain {}", sequence - 1L, sequenceDomain);
        return sequence - 1L;
    }

    public long getNextSequenceBlock(String sequenceDomain, long blockSize) {
        Accessor accessor = this.collectionProvider.getMapAccessor("seq", sequenceDomain);
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        long sequence = template.opsForHash().increment(accessor.getBucketName(), sequenceDomain, blockSize);
        Long startSequence = (Long)this.domainStartSequences.get(sequenceDomain);
        if (startSequence != null && sequence - blockSize < startSequence) {
            sequence = template.opsForHash().increment(accessor.getBucketName(), sequenceDomain, startSequence);
        }

        LOGGER.debug("Return next sequence {} with block size {} for sequence domain {}", new Object[]{sequence - blockSize, blockSize, sequenceDomain});
        return sequence - blockSize;
    }

    public void resetSequence(String sequenceDomain, long sequence) {
        Accessor accessor = this.collectionProvider.getMapAccessor("seq", sequenceDomain);
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        long currentSequence = 0L;

        try {
            currentSequence = this.getCurrentSequence(sequenceDomain);
            if (sequence <= currentSequence) {
                LOGGER.warn("Sequence {} to be reset is not greater than what is already in-store {} for sequence domain {}", new Object[]{sequence, currentSequence, sequenceDomain});
            }
        } catch (Exception var9) {
            LOGGER.warn("Unable to retrieve current sequence for {}, it may be a fresh reset", sequenceDomain);
        }

        sequence = Math.max(sequence, currentSequence);
        if (sequence > currentSequence) {
            template.opsForHash().put(accessor.getBucketName(), sequenceDomain, String.valueOf(sequence));
            LOGGER.debug("Reset next sequence to {} for sequence domain {}", sequence, sequenceDomain);
        } else {
            LOGGER.debug("Current sequence {} for sequence domain {} remains during reset", currentSequence, sequenceDomain);
        }

    }

    public long getCurrentSequence(String sequenceDomain) {
        Accessor accessor = this.collectionProvider.getMapAccessor("seq", sequenceDomain);
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        Object value = template.opsForHash().get(accessor.getBucketName(), sequenceDomain);
        if (value != null) {
            return value instanceof Long ? (Long)value : Long.parseLong(value.toString());
        } else {
            return 0L;
        }
    }
}
