package com.everhomes.promotion;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.messaging.AddressMessageRoutingHandler;
import com.everhomes.pusher.PusherAction;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.promotion.CreateOpPromotionCommand;
import com.everhomes.rest.promotion.GetOpPromotionActivityByPromotionId;
import com.everhomes.rest.promotion.ListOpPromotionActivityResponse;
import com.everhomes.rest.promotion.ListPromotionCommand;
import com.everhomes.rest.promotion.OpPromotionActionType;
import com.everhomes.rest.promotion.OpPromotionActivityDTO;
import com.everhomes.rest.promotion.OpPromotionAssignedScopeDTO;
import com.everhomes.rest.promotion.OpPromotionConditionType;
import com.everhomes.rest.promotion.OpPromotionOrderRangeCommand;
import com.everhomes.rest.promotion.OpPromotionRangePriceData;
import com.everhomes.rest.promotion.OpPromotionRegionPushingCommand;
import com.everhomes.rest.promotion.OpPromotionScopeType;
import com.everhomes.rest.promotion.OpPromotionSearchCommand;
import com.everhomes.rest.promotion.OpPromotionStatus;
import com.everhomes.rest.promotion.ScheduleTaskResourceType;
import com.everhomes.rest.promotion.ScheduleTaskStatus;
import com.everhomes.rest.promotion.UpdateOpPromotionCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.EhOpPromotionActivities;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class PromotionServiceImpl implements PromotionService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressMessageRoutingHandler.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private BusBridgeProvider busBridgeProvider;
    
    @Autowired
    private OpPromotionActivityProvider promotionActivityProvider;
    
    @Autowired
    private OpPromotionAssignedScopeProvider promotionAssignedScopeProvider;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private ScheduleTaskProvider scheduleTaskProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    BizHttpRestCallProvider bizHttpRestCallProvider;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    @Autowired
    private ContentServerService contentServerService;
    
    private String queueName = "promotion-push";
    
    @PostConstruct
    void setup() {
        String subcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhOpPromotionActivities.class, null);
        localBus.subscribe(subcribeKey, this);
        //localBus.unsubscribe(arg0, arg1);
        
        workerPoolFactory.getWorkerPool().addQueue(queueName);
     }
    
    @Override
    public void createPromotion(CreateOpPromotionCommand cmd) {
        //DaoHelper.publishDaoAction(DaoAction.CREATE, EhOpPromotionActivities.class, 5l);
        //localBus.publish("global", EhOpPromotionActivities.class.getName(), 5l);
        
        //TODO check login
        User user = UserContext.current().getUser();
        
        List<OpPromotionAssignedScopeDTO> scopes = cmd.getAssignedScopes();
        if(scopes == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Scopes is null");
        }
        
        for(OpPromotionAssignedScopeDTO scope : scopes) {
            if(scope.getScopeCode() == null || (scope.getScopeId() == null && (!scope.getScopeCode().equals(OpPromotionScopeType.ALL.getCode())))) {
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Scopes is null");        
            }
        }
        
        cmd.setAssignedScopes(null);
        
        OpPromotionActivity promotion = this.dbProvider.execute(new TransactionCallback<OpPromotionActivity>() {
            @Override
            public OpPromotionActivity doInTransaction(TransactionStatus arg0) {
                OpPromotionActivity activity = (OpPromotionActivity)ConvertHelper.convert(cmd, OpPromotionActivity.class);
                
                OpPromotionConditionType condType = OpPromotionConditionType.fromCode(activity.getPolicyType());
                if(condType == null) {
                    //TODO throw exception
                    return null;
                }
                
                if(condType == OpPromotionConditionType.ORDER_RANGE_VALUE) {
                    OpPromotionRangePriceData data = (OpPromotionRangePriceData)StringHelper.fromJsonString(activity.getPolicyData(), OpPromotionRangePriceData.class);
                    if(data == null) {
                        //TODO throw exception
                        return null;
                    }
                    
                    activity.setIntegralTag1(data.getFrom());
                    activity.setIntegralTag2(data.getTo());
                }
                
                activity.setStartTime(new Timestamp(cmd.getStartTime()));
                activity.setEndTime(new Timestamp(cmd.getEndTime()));
                //LOGGER.info("in=" + cmd.getStartTime() + ", curr=" + System.currentTimeMillis());
                activity.setCreatorUid(user.getId());
                activity.setStatus(OpPromotionStatus.ACTIVE.getCode());
                promotionActivityProvider.createOpPromotionActivity(activity);
                
                for(OpPromotionAssignedScopeDTO dto : scopes) {
                    OpPromotionAssignedScope scope = (OpPromotionAssignedScope)ConvertHelper.convert(dto, OpPromotionAssignedScope.class);
                    scope.setPromotionId(activity.getId());
                    promotionAssignedScopeProvider.createOpPromotionAssignedScope(scope);
                    }
                
                ScheduleTask task = new ScheduleTask();
                task.setNamespaceId(activity.getNamespaceId());
                task.setProcessCount(0);
                task.setProgress(0);
                task.setResourceId(activity.getId());
                task.setResourceType(ScheduleTaskResourceType.PROMOTION_ACTIVITY.getCode());
                task.setStatus(ScheduleTaskStatus.CREATING.getCode());
                scheduleTaskProvider.createScheduleTask(task);
                
                return activity;
            }
        });
        
        if(promotion != null) {
            String triggerName = OpPromotionConstant.SCHEDULE_TARGET_NAME + System.currentTimeMillis();
            String jobName = triggerName;
            
            Map<String, Object> map = new HashMap<String, Object>();
            //String cronExpression = "0/5 * * * * ?";
            map.put("id", promotion.getId().toString());
            map.put(OpPromotionConstant.SCHEDULE_TYPE, OpPromotionConstant.SCHEDULE_START);
            //scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, OpPromotionScheduleJob.class, map);
            
            scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getStartTime().getTime()), OpPromotionScheduleJob.class, map);
            
//            triggerName = "oppromotion-cron-" + System.currentTimeMillis();
//            jobName = triggerName;
//            Map<String, Object> map2 = new HashMap<String, Object>();
//            map.put("id", promotion.getId().toString());
//            map.put("type", "end");
//            scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getEndTime().getTime()), OpPromotionScheduleJob.class, map2);
            
            //this.broadcastEvent(DaoAction.CREATE, EhOpPromotionActivities.class, promotion.getId());
        }

    }
    
    @Override
    public void createRegionPushing(OpPromotionRegionPushingCommand regionCommand) {
        CreateOpPromotionCommand cmd = new CreateOpPromotionCommand();
        cmd.setActionData(regionCommand.getContent());
        cmd.setActionType(OpPromotionActionType.TEXT_ONLY.getCode());
        cmd.setStartTime(regionCommand.getStartTime());
        cmd.setEndTime(regionCommand.getEndTime());
        cmd.setPolicyType(OpPromotionConditionType.ALL.getCode());
        cmd.setNamespaceId(regionCommand.getNamespaceId());
        
        List<OpPromotionAssignedScopeDTO> scopes = new ArrayList<OpPromotionAssignedScopeDTO>();
        OpPromotionAssignedScopeDTO scope = new OpPromotionAssignedScopeDTO();
        //scope.setPromotionId(regionCommand.getScopeId());
        scope.setScopeCode(regionCommand.getScopeCode());
        scope.setScopeId(regionCommand.getScopeId());
        scopes.add(scope);
        cmd.setAssignedScopes(scopes);
        this.createPromotion(cmd);
    }
    
    @Override
    public void broadcastEvent(DaoAction action, Class<?> pojoClz, Long id) {
        LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
        
        String arg3 = "" + action.getName() + "." + pojoClz.getSimpleName();
        String arg1 = arg3 + "." + id.toString();
        
        //broadcast
        localBusSubscriber.onLocalBusMessage(null, arg1, id, arg3);
        
        DaoHelper.publishDaoAction(action, pojoClz, id);
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        //Must be
        try {
        	ExecutorUtil.submit(new Runnable() {
				@Override
				public void run() {
					LOGGER.info("start run.....");
					 Long id = (Long)arg2;
			         if(null == id) {
			              LOGGER.error("None of promotion");
			         } else {
			              LOGGER.debug("new promotion id= " + id);
			              
			              try {
				           
			            	  OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivityById(id);
				              if(promotion != null) {
					              	OpPromotionCondition condition = OpPromotionUtils.getConditionFromPromotion(promotion);
					              	OpPromotionActivityContext ctx = new OpPromotionActivityContext(promotion);
					                    
					                //Create local condition event
					                condition.createCondition(ctx);
				               }
				              
			              } catch(Exception exx) {
			            	  LOGGER.error("execute promotion error promotionId=" + id, exx);
			              }

			         }
				}
			});
			
        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        } finally{
        	
        }

        return Action.none;
    }
    
    @Override
    public ListOpPromotionActivityResponse listPromotion(ListPromotionCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        List<OpPromotionActivity> promotions = promotionActivityProvider.listOpPromotion(locator, count);
        
        ListOpPromotionActivityResponse resp = new ListOpPromotionActivityResponse();
        if(promotions != null) {
            List<OpPromotionActivityDTO> dtos = new ArrayList<OpPromotionActivityDTO>();
            for(OpPromotionActivity op : promotions) {
                OpPromotionActivityDTO dto = ConvertHelper.convert(op, OpPromotionActivityDTO.class);
                
                if(dto != null) {
//                    ScheduleTask task = scheduleTaskProvider.getSchduleTaskByPromotionId(dto.getId());
//                    if(task != null) {
//                        dto.setPushCount(task.getProcessCount());
//                    }
                    List<OpPromotionAssignedScope> scopes = promotionAssignedScopeProvider.getOpPromotionScopeByPromotionId(dto.getId());
                    
                    List<OpPromotionAssignedScopeDTO> scopeDtos = new ArrayList<OpPromotionAssignedScopeDTO>();
                    for(OpPromotionAssignedScope r : scopes) {
                        scopeDtos.add(ConvertHelper.convert(r, OpPromotionAssignedScopeDTO.class));
                    }
                    dto.setAssignedScopes(scopeDtos);
                    
                    if(dto.getCreatorUid() != null) {
                        User user = userProvider.findUserById(dto.getCreatorUid());
                        if(user != null) {
                            if(user.getNickName() != null && user.getNickName() != "") {
                                dto.setNickName(user.getNickName());
                            } else {
                                dto.setNickName(user.getAccountName());
                            }
                        }
                    }
                    
                    
                    dtos.add(dto);
                }
            }
            
            resp.setPromotions(dtos);
        }
        resp.setNextPageAnchor(locator.getAnchor());
        
        return resp;
    }
    
    private String getScopeNameById(OpPromotionAssignedScope scope) {
        OpPromotionScopeType t = OpPromotionScopeType.fromCode(scope.getScopeCode());
        
        switch(t) {
         case ALL:
             return "";
         case COMMUNITY:
             return "";
         case ORGANIZATION:
             return "";
         case CITY:
             return "";
         default:
             return "";
        }
    }
    
    @Override
    public OpPromotionActivityDTO getPromotionById(GetOpPromotionActivityByPromotionId cmd) {
        OpPromotionActivity promotion = this.promotionActivityProvider.getOpPromotionActivityById(cmd.getPromotionId());
        if(promotion != null) {
            OpPromotionActivityDTO dto = ConvertHelper.convert(promotion, OpPromotionActivityDTO.class);
            List<OpPromotionAssignedScope> scopes = promotionAssignedScopeProvider.getOpPromotionScopeByPromotionId(dto.getId());
            
            List<OpPromotionAssignedScopeDTO> scopeDtos = new ArrayList<OpPromotionAssignedScopeDTO>();
            for(OpPromotionAssignedScope r : scopes) {
                OpPromotionAssignedScopeDTO sdto = ConvertHelper.convert(r, OpPromotionAssignedScopeDTO.class);
                sdto.setName(getScopeNameById(r));
                scopeDtos.add(sdto);
            }
            dto.setAssignedScopes(scopeDtos);
            
            if(dto.getCreatorUid() != null) {
                User user = userProvider.findUserById(dto.getCreatorUid());
                if(user != null) {
                    if(user.getNickName() != null && user.getNickName() != "") {
                        dto.setNickName(user.getNickName());
                    } else {
                        dto.setNickName(user.getAccountName());
                    }
                }
            }
            
            if(promotion.getIconUri() != null) {
                String url = contentServerService.parserUri(promotion.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                dto.setIconUrl(url);
            }
            
            
            return dto;
        }
        
        return null;
    }
    
    @Override
    public void newOrderPriceEvent(OpPromotionOrderRangeCommand cmd) {
        final Job job = new Job(PriceOrderAction.class.getName(),
                new Object[]{ cmd.getUserId().toString(), Long.toString(cmd.getPrice().longValue()) });
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }
    
    @Override
    public void onNewOrderPriceJob(Long userId, Long price) {
        
        User user = userProvider.findUserById(userId);
        if(user == null) {
            //TODO throw exception
            return;
        }
        
        ListingLocator locator = new ListingLocator();
        int count = 100;
        List<OpPromotionActivity> promotions = promotionActivityProvider.listOpPromotionByPriceRange(locator, count, price);
        
        while(promotions != null && promotions.size() > 0) {
            for(OpPromotionActivity promotion : promotions) {
                OpPromotionAction action = OpPromotionUtils.getActionFromPromotion(promotion);
                OpPromotionActivityContext ctx = new OpPromotionActivityContext(promotion);
                ctx.setUser(user);
                ctx.setNeedUpdate(true);
                action.fire(ctx);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
            
            promotions = promotionActivityProvider.listOpPromotionByPriceRange(locator, count, price);
        }
        
    }
    
    @Override
    public void updateOpPromotionActivity(OpPromotionActivity obj) {
        //Update status, and broadcast event to all server
        promotionActivityProvider.updateOpPromotionActivity(obj);
        this.broadcastEvent(DaoAction.MODIFY, EhOpPromotionActivities.class, obj.getId());
    }
    
    @Override
    public void closeOpPromotion(OpPromotionActivity promotion) {
        if(promotion != null && !promotion.getStatus().equals(OpPromotionStatus.COMPLETE.getCode())) {
            promotion.setStatus(OpPromotionStatus.INACTIVE.getCode());
            updateOpPromotionActivity(promotion);    
        }
        
    }
    
    @Override
    public void finishOpPromotion(OpPromotionActivity promotion) {
        if(promotion != null) {
            promotion.setStatus(OpPromotionStatus.COMPLETE.getCode());
            updateOpPromotionActivity(promotion);    
        }
        
    }
    
    @Override
    public void closeOpPromotion(GetOpPromotionActivityByPromotionId cmd) {
        OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivityById(cmd.getPromotionId());
        closeOpPromotion(promotion);
    }
    
    @Override
    public OpPromotionActivity addPushCountByPromotionId(Long id, int count) {
        return this.dbProvider.execute(new TransactionCallback<OpPromotionActivity>() {
            @Override
            public OpPromotionActivity doInTransaction(TransactionStatus arg0) {
                OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivityById(id);
                if(promotion != null) {
                    promotion.setPushCount(promotion.getPushCount()+count);
                    promotionActivityProvider.updateOpPromotionActivity(promotion);
                }
                
                return promotion;
            }
        });
    }
    
    @Override
    public ListOpPromotionActivityResponse searchPromotion(OpPromotionSearchCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<OpPromotionActivity> promotions = null;
        
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            promotions = promotionActivityProvider.listOpPromotion(locator, count);
        } else {
            promotions = promotionActivityProvider.searchOpPromotionByKeyword(locator, count, cmd.getKeyword());
        }
        
        ListOpPromotionActivityResponse resp = new ListOpPromotionActivityResponse();
        if(promotions != null) {
            List<OpPromotionActivityDTO> dtos = new ArrayList<OpPromotionActivityDTO>();
            for(OpPromotionActivity op : promotions) {
                OpPromotionActivityDTO dto = ConvertHelper.convert(op, OpPromotionActivityDTO.class);
                
                if(dto != null) {
                    List<OpPromotionAssignedScope> scopes = promotionAssignedScopeProvider.getOpPromotionScopeByPromotionId(dto.getId());
                    
                    List<OpPromotionAssignedScopeDTO> scopeDtos = new ArrayList<OpPromotionAssignedScopeDTO>();
                    for(OpPromotionAssignedScope r : scopes) {
                        scopeDtos.add(ConvertHelper.convert(r, OpPromotionAssignedScopeDTO.class));
                    }
                    dto.setAssignedScopes(scopeDtos);
                    
                    if(dto.getCreatorUid() != null) {
                        User user = userProvider.findUserById(dto.getCreatorUid());
                        if(user != null) {
                            if(user.getNickName() != null && user.getNickName() != "") {
                                dto.setNickName(user.getNickName());
                            } else {
                                dto.setNickName(user.getAccountName());
                            }
                        }
                    }
                    
                    if(dto.getIconUri() != null) {
                        String url = contentServerService.parserUri(dto.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                        dto.setIconUrl(url);
                    }
                    
                    dtos.add(dto);
                }
            }
            
            resp.setPromotions(dtos);
        }
        resp.setNextPageAnchor(locator.getAnchor());
        
        return resp;
    }
    
    @Override
    public void udpateOpPromotion(UpdateOpPromotionCommand cmd) {
        OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivityById(cmd.getId());
        if(cmd.getTitle() != null && (!cmd.getTitle().isEmpty())) {
            promotion.setTitle(cmd.getTitle());    
        }
        
        if(cmd.getDescription() != null && (!cmd.getDescription().isEmpty())) {
            promotion.setDescription(cmd.getDescription());    
        }
        
        promotionActivityProvider.updateOpPromotionActivity(promotion);
    }
    
    @Override
    public void bizFetchCoupon(Long userId, Long couposId) {
        Integer nonce = (int)(Math.random()*1000);
        Long timestamp = System.currentTimeMillis();       
        Map<String,String> params = new HashMap<String, String>();
        params.put("nonce", nonce+"");
        params.put("timestamp", timestamp+"");
        params.put("promotionNo", couposId+"");
        params.put("userId", userId+"");
        
        try {
            bizHttpRestCallProvider.restCall("rest/openapi/promotion/fetch", params, new ListenableFutureCallback<ResponseEntity<String>> () {

                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    String body = result.getBody();
                    if(body.indexOf("true") < 0) {
                        LOGGER.warn(body);    
                    }
                    
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("fetch error", ex);
                }
                
            });
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("fetch error", e);
        }
    }
}
