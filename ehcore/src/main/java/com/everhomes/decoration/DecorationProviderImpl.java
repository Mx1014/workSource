package com.everhomes.decoration;

import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.sequence.SequenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DecorationProviderImpl implements  DecorationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDecorationSetting(DecorationIllustrationDTO setting) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(.class));
        return null;
    }
}
