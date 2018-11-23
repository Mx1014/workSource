package com.everhomes.openapi.zhenzhihui;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhTickets;
import com.everhomes.server.schema.tables.daos.EhTicketsDao;
import com.everhomes.server.schema.tables.records.EhTicketsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IdToken;
import com.everhomes.util.WebTokenGenerator;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TicketProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketProvider.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    public String createTicket4User(Long userId, String redirectCode) throws Exception {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTickets.class));

        String token = WebTokenGenerator.getInstance().toWebToken(new IdToken(id));

        EhTickets ticket = new EhTickets();
        ticket.setId(id);
        ticket.setUserId(userId);
        ticket.setTicket(token);
        ticket.setRedirectCode(redirectCode);
        ticket.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EhTicketsDao dao = new EhTicketsDao(context.configuration());
        dao.insert(ticket);
        return token;
    }

    public EhTickets getUserIdByTicket(String ticket) {
        final EhTickets result[] = new EhTickets[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhTickets.class), null, (DSLContext context, Object reducingContext) -> {
            EhTicketsRecord record = (EhTicketsRecord)context.select()
                    .from(Tables.EH_TICKETS)
                    .where(Tables.EH_TICKETS.TICKET.eq(ticket))
                    .fetchOne();

            if(record != null) {
                result[0] = ConvertHelper.convert(record, EhTickets.class);
                return false;
            }
            return true;
        });
        return result[0];
    }

}
