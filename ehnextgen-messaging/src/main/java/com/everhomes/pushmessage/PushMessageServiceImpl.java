package com.everhomes.pushmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.pushmessage.ListPushMessageResultCommand;
import com.everhomes.rest.pushmessage.PushMessageResultDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PushMessageServiceImpl implements PushMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageServiceImpl.class);
    
    @Autowired
    PushMessageProvider pushMessageProvider;
    
    @Autowired
    PushMessageResultProvider pushMessageResultProvider;
    
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    private String queueName = "pushmessage";
    
    private Random r = new Random();
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }
    
    @Override
    public void createPushMessage(PushMessage pushMessage) {
        pushMessage.setStatus(PushMessageStatus.Ready.getCode());
        pushMessage.setPushCount(new Long(r.nextInt()));
        pushMessageProvider.createPushMessage(pushMessage);
        
        final Job job = new Job(PushMessageAction.class.getName(),
                new Object[]{String.valueOf(pushMessage.getId()), String.valueOf(pushMessage.getPushCount()) });
        
        if(pushMessage.getStartTime() != null && pushMessage.getStartTime().getTime() > System.currentTimeMillis()) {
            //A delay push
            jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job, pushMessage.getStartTime().getTime());
        } else {
            //Push right now
            jesqueClientFactory.getClientPool().enqueue(queueName, job);    
        }
        
    }

    @Override
    public PushMessage getPushMessageById(Long id) {
        return pushMessageProvider.getPushMessageById(id);
    }

    @Override
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count) {
        return this.pushMessageProvider.queryPushMessages(locator, count);
    }

    @Override
    public PushMessageResult getPushMessageResultById(Long id) {
        return this.pushMessageResultProvider.getPushMessageResultById(id);
    }

    @Override
    public List<PushMessageResult> queryPushMessageResultByIdentify(CrossShardListingLocator locator, int count,
            Long targetUserId) {
        return this.pushMessageResultProvider.queryPushMessageResultByIdentify(locator, count, targetUserId);
    }

    @Override
    public boolean updatePushMessage(PushMessage pushMessage) {
        PushMessage oldMessage = this.pushMessageProvider.getPushMessageById(pushMessage.getId());
        if(oldMessage != null && oldMessage.getStatus().intValue() == PushMessageStatus.Ready.getCode()) {
            if(pushMessage.getStartTime() != oldMessage.getStartTime()) {
                pushMessage.setPushCount(new Long(r.nextInt()));    
            }
            //Update message first
            this.pushMessageProvider.updatePushMessage(pushMessage);
            
            if(pushMessage.getStartTime() != oldMessage.getStartTime()) {
                //Add a new job
                final Job job = new Job(PushMessageAction.class.getName(),
                        new Object[]{String.valueOf(pushMessage.getId()), String.valueOf(pushMessage.getPushCount()) });
                if(pushMessage.getStartTime() != null && pushMessage.getStartTime().getTime() > System.currentTimeMillis()) {
                    jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job, pushMessage.getStartTime().getTime());
                } else {
                    jesqueClientFactory.getClientPool().enqueue(queueName, job);    
                    }    
                }
            
            
            return true;
        }
        
        return false;
    }

    @Override
    public void deleteByPushMesageId(Long id) {
        this.pushMessageProvider.deleteByPushMesageId(id);
    }
    
    @Override
    public List<PushMessageResultDTO> queryPushMessageResult(CrossShardListingLocator locator, ListPushMessageResultCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        User user = UserContext.current().getUser();
        List<PushMessageResultDTO> dtos = new ArrayList<PushMessageResultDTO>();
        Long targetUserId = null;
        if(cmd.getIdenfity() != null) {
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(user.getNamespaceId(), cmd.getIdenfity());
            if(userIdentifier != null) {
                targetUserId =  userIdentifier.getOwnerUid();
            } else {
                LOGGER.error("User not found, userIdentifier=" + cmd.getIdenfity());
            }
        }
        List<PushMessageResult> msgs = pushMessageResultProvider.queryPushMessageResultByIdentify(locator, pageSize, targetUserId);
        for(int i = 0; i < msgs.size(); i++) {
            PushMessage pMsg = pushMessageProvider.getPushMessageById(msgs.get(i).getMessageId());
            PushMessageResult msg = msgs.get(i);
            PushMessageResultDTO dto;
            if(pMsg != null) {
                dto = ConvertHelper.convert(r, PushMessageResultDTO.class);
            } else {
                dto = new PushMessageResultDTO();
                }
            dto.setId(msg.getId());
            dto.setUserId(msg.getUserId());
            dto.setIdentifierToken(msg.getIdentifierToken());
            dtos.add(dto);
        }
        
        return dtos;
    }
    

}
