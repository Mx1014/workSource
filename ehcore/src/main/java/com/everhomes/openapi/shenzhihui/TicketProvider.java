package com.everhomes.openapi.shenzhihui;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhTickets;
import com.everhomes.server.schema.tables.daos.EhTicketsDao;
import com.everhomes.util.IdToken;
import com.everhomes.util.WebTokenGenerator;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketProvider.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    public String createTicket4User(Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

//        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTickets.class));
////        code.setId(id);
////        code.setCode(WebTokenGenerator.getInstance().toWebToken(new IdToken(id)));
//        String ticketStr = WebTokenGenerator.getInstance().toWebToken(new IdToken(id));
//
//        EhTicket ticket = new EhTicket();
//        ticket.setId(id);
//        ticket.setTicket(ticketStr);
//
//
//        EhTicketsDao dao = new EhTicketsDao(context.configuration());
//        dao.insert(code);
        return null;
    }
}
