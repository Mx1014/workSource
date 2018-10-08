// @formatter:off
package com.everhomes.point;

import com.everhomes.PictureValidate.PictureValidateService;
import com.everhomes.bus.*;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.point.rpc.PointServiceRPCRest;
import com.everhomes.promotion.BizHttpRestCallProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.point.*;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserTreasureDTO;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.pojos.EhPointBanners;
import com.everhomes.server.schema.tables.pojos.EhPointScores;
import com.everhomes.server.schema.tables.pojos.EhPointTutorials;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Created by xq.tian on 2017/11/24.
 */
@Service
public class PointServiceImpl implements PointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private PointScoreProvider pointScoreProvider;

    @Autowired
    private PointSystemProvider pointSystemProvider;

    @Autowired
    private PointLogProvider pointLogProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private PointTutorialProvider pointTutorialProvider;

    @Autowired
    private PointTutorialToPointRuleMappingProvider pointTutorialToPointRuleMappingProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private PointGoodProvider pointGoodProvider;

    @Autowired
    private PointRuleProvider pointRuleProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private PointRuleCategoryProvider pointRuleCategoryProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PointEventLogScheduler pointEventLogScheduler;

    @Autowired
    private PointLocalBusSubscriber pointLocalBusSubscriber;

    @Autowired
    private PointRuleConfigProvider pointRuleConfigProvider;

    @Autowired
    private PointBannerProvider pointBannerProvider;

    @Autowired
    private BizHttpRestCallProvider bizHttpRestCallProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private PictureValidateService pictureValidateService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private BusBridgeProvider busBridgeProvider;
    
    
    @Autowired
    private PointServiceRPCRest pointServiceRPCRest;
    
    

    @Override
    public ListPointSystemsResponse listPointSystems(ListPointSystemsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointSystem> pointSystems = pointSystemProvider.listPointSystems(cmd.getNamespaceId(), pageSize, locator);

        ListPointSystemsResponse response = new ListPointSystemsResponse();
        response.setSystems(pointSystems.stream().map(this::toPointSystemDTO).collect(Collectors.toList()));
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public PointSystemDTO enablePointSystem(PointSystemIdCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointCommonStatus status = PointCommonStatus.fromCode(pointSystem.getStatus());
        if (status != PointCommonStatus.ENABLED) {
            dbProvider.execute(s -> {
                List<PointSystem> enabledPointSystems = pointSystemProvider.getEnabledPointSystems(pointSystem.getNamespaceId());
                for (PointSystem system : enabledPointSystems) {
                    system.setStatus(PointCommonStatus.DISABLED.getCode());
                    pointSystemProvider.updatePointSystem(system);
                }

                pointSystem.setStatus(PointCommonStatus.ENABLED.getCode());
                pointSystemProvider.updatePointSystem(pointSystem);
                return true;
            });
        }
        return toPointSystemDTO(pointSystem);
    }

    @Override
    public PointSystemDTO disablePointSystem(PointSystemIdCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointCommonStatus status = PointCommonStatus.fromCode(pointSystem.getStatus());
        if (status != PointCommonStatus.DISABLED) {
            pointSystem.setStatus(PointCommonStatus.DISABLED.getCode());
            pointSystemProvider.updatePointSystem(pointSystem);
        }
        return toPointSystemDTO(pointSystem);
    }

    @Override
    public void deletePointSystem(PointSystemIdCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointCommonStatus status = PointCommonStatus.fromCode(pointSystem.getStatus());
        if (status != PointCommonStatus.INACTIVE) {
            pointSystem.setStatus(PointCommonStatus.INACTIVE.getCode());

            dbProvider.execute(s -> {
                pointSystemProvider.updatePointSystem(pointSystem);
                pointRuleConfigProvider.deleteBySystemId(pointSystem.getId());
                return true;
            });
        }
    }

    @Override
    public PointRuleDTO getPointRule(GetPointRuleCommand cmd) {
        // ValidatorUtil.validate(cmd);
        // List<PointRule> pointRules = pointRuleProvider.listPointRuleByEventName(
        //         cmd.getNamespaceId(), cmd.getSystemId(), cmd.getEventName());
        return null;
    }

    @Override
    public void restartEventLogScheduler() {
        // 发布到本地
        LocalEventBus.publish("PointEventLogSchedulerRestart", null, null);

        // 发布到远程
        LocalBusSubscriber subscriber = (LocalBusSubscriber) this.busBridgeProvider;
        subscriber.onLocalBusMessage(this, "PointEventLogSchedulerRestart", null, "");
    }

    @Override
    public void reloadEventMapping() {
        pointLocalBusSubscriber.initPointRuleCategoryQueue();
    }

    @Override
    public PointRuleDTO createPointRule(CreatePointRuleCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointRule rule = ConvertHelper.convert(cmd, PointRule.class);


        return toPointRuleDTO(rule);
    }

    @Override
    public void createPointRuleToEventMapping(CreatePointRuleToEventMappingCommand cmd) {
        ValidatorUtil.validate(cmd);
    }

   /* @Override
    public UserTreasureDTO getPointTreasure() {
        UserTreasureDTO point = new UserTreasureDTO();
        point.setCount(0L);
        point.setStatus(TrueOrFalseFlag.FALSE.getCode());
        point.setUrlStatus(TrueOrFalseFlag.FALSE.getCode());

        GetEnabledPointSystemCommand cmd = new GetEnabledPointSystemCommand();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        cmd.setNamespaceId(namespaceId);
        GetEnabledPointSystemResponse pointSystemResponse = this.getEnabledPointSystem(cmd);

        if (pointSystemResponse == null || pointSystemResponse.getSystems() == null || pointSystemResponse.getSystems().size() == 0) {
            return point;
        }
        point.setStatus(TrueOrFalseFlag.TRUE.getCode());

        Long currentUserId = UserContext.currentUserId();
        if (currentUserId == null) {
            return point;
        }

        PointSystemDTO system = pointSystemResponse.getSystems().get(0);

        GetUserPointCommand pointCommand = new GetUserPointCommand();
        pointCommand.setUid(currentUserId);
        pointCommand.setSystemId(system.getId());
        PointScoreDTO userPoint = this.getUserPoint(pointCommand);
        if (userPoint != null) {
            point.setCount(userPoint.getScore());
        }
        String url = getPointSystemUrl(system.getId());
        point.setUrl(url);

        TrueOrFalseFlag flag = TrueOrFalseFlag.fromCode(system.getPointExchangeFlag());
        if (flag != null) {
            point.setUrlStatus(flag.getCode());
        }
        return point;
    }*/

    /**
     * 调用积分新系统的接口获取用户积分
     * @return
     */
    @Override
    public UserTreasureDTO getPointTreasure() {
        UserTreasureDTO point = new UserTreasureDTO();
        point.setCount(0L);
        //设置积分默认不可见
        point.setStatus(TrueOrFalseFlag.FALSE.getCode());
        point.setUrlStatus(TrueOrFalseFlag.FALSE.getCode());
       //可不可见按配置来展示
        String pointStatus = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "point.show.flag", "");
        String pointUrlStatus = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "point.url.flag", "");
       if(StringUtils.isNotBlank(pointStatus)&& pointStatus.equals("1")){
           point.setStatus(TrueOrFalseFlag.TRUE.getCode());
       }
        if(StringUtils.isNotBlank(pointUrlStatus)&& pointUrlStatus.equals("1")){
            point.setUrlStatus(TrueOrFalseFlag.TRUE.getCode());
        }

        GetUserPointCommand cmd = new GetUserPointCommand();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        cmd.setNamespaceId(namespaceId);
        Long currentUserId = UserContext.currentUserId();
        if (currentUserId == null) {
            return point;
        }
        cmd.setUid(currentUserId);
        PointScoreDTO dto = null ;
        //update by huangliangming 远程调时所发生的一切问题不能影响该接口的运行,只当是没取到积分而已.
        try{
             dto = pointServiceRPCRest.getUserPoint(cmd);
        }catch(Exception e){
            LOGGER.error("something error happen while RPC to point system . e:{}",e);
        }

        if(dto != null){
            point.setCount(dto.getScore());
        }

        String url = getPointSystemUrl(1L);
        point.setUrl(url);

        return point;
    }

    @Override
    public PointRuleDTO updatePointRule(UpdatePointRuleCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointRuleConfig ruleConfig = pointRuleConfigProvider.findByRuleIdAndSystemId(cmd.getSystemId(), cmd.getId());
        if (ruleConfig == null) {
            PointRule rule = pointRuleProvider.findById(cmd.getId());
            ruleConfig = ConvertHelper.convert(rule, PointRuleConfig.class);
            if (cmd.getDescription() != null) {
                ruleConfig.setDescription(cmd.getDescription().trim());
            }

            ruleConfig.setNamespaceId(pointSystem.getNamespaceId());
            ruleConfig.setRuleId(rule.getId());
            ruleConfig.setLimitType(cmd.getLimitType());
            ruleConfig.setLimitData(cmd.getLimitData());
            ruleConfig.setStatus(cmd.getStatus());
            ruleConfig.setPoints(cmd.getPoints());
            ruleConfig.setSystemId(cmd.getSystemId());
            pointRuleConfigProvider.createPointRuleConfig(ruleConfig);
        } else {
            if (cmd.getDescription() != null) {
                ruleConfig.setDescription(cmd.getDescription().trim());
            }
            ruleConfig.setLimitType(cmd.getLimitType());
            ruleConfig.setLimitData(cmd.getLimitData());
            ruleConfig.setStatus(cmd.getStatus());
            ruleConfig.setPoints(cmd.getPoints());
            pointRuleConfigProvider.updatePointRuleConfig(ruleConfig);
        }
        return toPointRuleDTO(ruleConfig);
    }

    private PointRuleDTO toPointRuleDTO(PointRuleConfig ruleConfig) {
        PointRule rule = pointRuleProvider.findById(ruleConfig.getRuleId());
        PointRuleDTO ruleDTO = ConvertHelper.convert(rule, PointRuleDTO.class);
        if (ruleConfig.getDescription() != null) {
            ruleDTO.setDescription(ruleConfig.getDescription().trim());
        }
        ruleDTO.setLimitType(ruleConfig.getLimitType());
        ruleDTO.setLimitData(ruleConfig.getLimitData());
        ruleDTO.setStatus(ruleConfig.getStatus());
        ruleDTO.setPoints(ruleConfig.getPoints());
        return ruleDTO;
    }

    @Override
    public String getPointSystemUrl(Long systemId) {
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.HOME_URL, "");
        String pointUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.POINT_DETAIL_PATH, "");
        return homeUrl + String.format(pointUrl, systemId);
    }

    @Override
    public GetEnabledPointSystemResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);
        List<PointSystem> systems = pointSystemProvider.getEnabledPointSystems(cmd.getNamespaceId());
        List<PointSystemDTO> dtoList = systems.stream().map(this::toPointSystemDTO).collect(Collectors.toList());
        return new GetEnabledPointSystemResponse(dtoList);
    }

    @Override
    public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();
        Long uid = cmd.getUid() != null ? cmd.getUid() : UserContext.currentUserId();

        PointScoreDTO scoreDTO = pointScoreProvider.findUserPointScore(namespaceId, cmd.getSystemId(), uid, PointScoreDTO.class);
        if (scoreDTO == null) {
            scoreDTO = new PointScoreDTO();
            scoreDTO.setScore(0L);
        }
        scoreDTO.setPointName(pointSystem.getPointName());
        return scoreDTO;
    }

    @Override
    public PointSystemDTO getPointSystem(GetPointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);
        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        return toPointSystemDTO(pointSystem);
    }

    @Override
    public ListPointLogsResponse listPointLogs(ListPointLogsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointLog> logs = pointLogProvider.listPointLogs(cmd, locator);
        List<PointLogDTO> logDTOS = logs.stream().map(this::toPointLogDTO).collect(Collectors.toList());

        ListPointLogsResponse response = new ListPointLogsResponse();
        response.setLogs(logDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private PointLogDTO toPointLogDTO(PointLog pointLog) {
        PointLogDTO dto = ConvertHelper.convert(pointLog, PointLogDTO.class);
        if (StringUtils.isEmpty(dto.getRuleName())) {
            dto.setRuleName(pointLog.getDescription());
        }
        return dto;
    }

    @Override
    public ListPointGoodsResponse listPointGoods(ListPointGoodsCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long pageNo = cmd.getPageAnchor() != null ? cmd.getPageAnchor() : 0;

        List<PointGood> goods = pointGoodProvider.listPointGood(pointSystem.getNamespaceId(), pointSystem.getId(), null, -1, new ListingLocator());
        ListPointGoodsResponse response = fetchPointGoodsFromBiz(pointSystem.getNamespaceId(), 1L, 999, goods, new HashMap<>());

        // 把电商那边已经取消的商品删除
        List<String> list1 = response.getGoods().parallelStream().map(r -> r.getShopNumber() + ":" + r.getNumber()).collect(Collectors.toList());
        List<PointGood> list2 = goods.parallelStream().filter(r -> !list1.contains(r.getShopNumber() + ":" + r.getNumber())).collect(Collectors.toList());

        if (list2.size() > 0) {
            pointGoodProvider.deletePointGoods(list2);
        }

        // 自己分页
        List<PointGoodDTO> subGoods = null;
        if (response.getGoods().size() > (pageSize * pageNo)) {
            int toIndex = (int) (pageSize * pageNo + pageSize);
            toIndex = toIndex < response.getGoods().size() ? toIndex : response.getGoods().size();
            subGoods = response.getGoods().subList((int) (pageSize * pageNo), toIndex);

            if (toIndex < response.getGoods().size()) {
                response.setNextPageAnchor(pageNo + 1);
            }
        }
        response.setGoods(subGoods);
        return response;
    }

    private PointGoodDTO commodityToGoodDTO(PointCommodity commodity) {
        PointGoodDTO dto = new PointGoodDTO();
        dto.setId(0L);
        dto.setDetailUrl(commodity.getDetailUrl());
        dto.setPosterUrl(commodity.getDefaultPic());
        dto.setNumber(commodity.getCommoNo());
        dto.setDisplayName(commodity.getCommoName());
        dto.setSoldAmount(commodity.getSellNum());
        dto.setOriginalPrice(commodity.getPrice());
        dto.setDiscountPrice(commodity.getDeductionMoney());
        dto.setStatus(PointCommonStatus.DISABLED.getCode());
        dto.setPoints(commodity.getDeductionIntegral());
        dto.setShopNumber(commodity.getShopNo());
        return dto;
    }

    @Override
    public ListPointGoodsResponse listEnabledPointGoods(ListPointGoodsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long pageNo = cmd.getPageAnchor() != null ? cmd.getPageAnchor() : 1;
        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();

        List<PointGood> enabledGoods = pointGoodProvider.listEnabledPointGoods(namespaceId, cmd.getSystemId(), pageSize, new ListingLocator());

        if (enabledGoods.size() == 0) {
            return new ListPointGoodsResponse();
        }

        Map<String, Object> params = new HashMap<>();
        List<ShopCommodityCmd> shopCommodityCmds = new ArrayList<>();
        for (PointGood good : enabledGoods) {
            ShopCommodityCmd shopCmd = new ShopCommodityCmd();
            shopCmd.setCommoNo(good.getNumber());
            shopCmd.setShopNo(good.getShopNumber());
            shopCommodityCmds.add(shopCmd);
        }
        params.put("shopCommoditys", shopCommodityCmds);

        ListPointGoodsResponse response = fetchPointGoodsFromBiz(namespaceId, pageNo, pageSize, enabledGoods, params);
        response.getGoods().sort(getPointGoodDTOComparator());
        return response;
    }

    private Comparator<PointGoodDTO> getPointGoodDTOComparator() {
        Comparator<PointGoodDTO> comparator = (o1, o2) -> {
            Timestamp o1TopTime = o1.getTopTime();
            Timestamp o2TopTime = o2.getTopTime();

            if (o1TopTime != null && o2TopTime != null) {
                return o1TopTime.compareTo(o2TopTime);
            }
            if (o1TopTime != null) {
                return 1;
            }
            if (o2TopTime != null) {
                return -1;
            }
            return 0;
        };
        return comparator.reversed();
    }

    @Override
    public CheckUserInfoResponse checkUserInfo(CheckUserInfoCommand cmd) {
        ValidatorUtil.validate(cmd);

        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();

        User user = userService.findUserByIndentifier(namespaceId, cmd.getPhone());
        if (user == null) {
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST,
                    "cannot find user information");
        }

        UserInfo userInfo = ConvertHelper.convert(user, UserInfo.class);
        userInfo.setPhones(Collections.singletonList(cmd.getPhone()));

        CheckUserInfoResponse response = new CheckUserInfoResponse();
        response.setUserInfo(userInfo);
        response.setCaptchaImg(pictureValidateService.newPicture(cmd.getSessionId()));
        return response;
    }

    @Override
    public PointScoreDTO getUserPointForOpenAPI(GetUserPointCommand cmd) {
        ValidatorUtil.validate(cmd);

        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();
        Long uid = cmd.getUid() != null ? cmd.getUid() : UserContext.currentUserId();

        PointScoreDTO scoreDTO = null;
        List<PointSystem> enabledPointSystems = pointSystemProvider.getEnabledPointSystems(namespaceId);

        if (enabledPointSystems != null && enabledPointSystems.size() > 0) {
            PointSystem system = enabledPointSystems.get(0);

            scoreDTO = pointScoreProvider.findUserPointScore(namespaceId, system.getId(), uid, PointScoreDTO.class);
            if (scoreDTO == null) {
                scoreDTO = new PointScoreDTO();
                scoreDTO.setScore(0L);
            }
            scoreDTO.setPointName(system.getPointName());
        }
        return scoreDTO;
    }

    private ListPointGoodsResponse fetchPointGoodsFromBiz(
            Integer namespaceId, Long pageNo, int pageSize, List<PointGood> goods, Map<String, Object> params) {
        Map<String, Object> bodyMap = new HashMap<>();
        params.put("namespaceId", namespaceId);
        params.put("pageSize", pageSize);
        params.put("pageNo", pageNo);

        bodyMap.put("body", params);

        String paramJson = StringHelper.toJsonString(bodyMap);

        ResponseEntity<String> entity = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetch point goods from biz, param = {}", paramJson);
            }
            entity = bizHttpRestCallProvider.syncRestCall(
                    "/rest/openapi/commodity/queryEnabledPointCommodityByPage", paramJson);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fetch point goods from biz, response = {}", entity.getBody());
            }
        } catch (Exception e) {
            LOGGER.error("Point biz call error", e);
            throw RuntimeErrorException.errorWith(e, PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_BIZ_CALL_ERROR_CODE,
                    "Point biz call error");
        }

        Map<String, PointGood> shopNoAndCommNoToCommMap =
                goods.stream().collect(Collectors.toMap(r -> r.getShopNumber() + r.getNumber(), r -> r));

        List<PointGoodDTO> dtoList = new ArrayList<>();
        QueryEnabledPointCommodityByPageResponse response = (QueryEnabledPointCommodityByPageResponse)
                StringHelper.fromJsonString(entity.getBody(), QueryEnabledPointCommodityByPageResponse.class);

        for (PointCommodity commodity : response.getBody().getRows()) {
            PointGoodDTO dto = commodityToGoodDTO(commodity);
            PointGood good = shopNoAndCommNoToCommMap.get(commodity.getShopNo() + commodity.getCommoNo());
            if (good != null) {
                dto.setId(good.getId());
                dto.setStatus(good.getStatus());
                dto.setTopStatus(good.getTopStatus());
                dto.setTopTime(good.getTopTime());
            }
            dtoList.add(dto);
        }

        ListPointGoodsResponse resp = new ListPointGoodsResponse();
        resp.setGoods(dtoList);
        if (response.getBody().getHasNext()) {
            resp.setNextPageAnchor(pageNo + 1);
        }
        return resp;
    }

    @Override
    public ListPointTutorialResponse listPointTutorials(ListPointTutorialsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointTutorial> tutorials = pointTutorialProvider.listPointTutorials(cmd.getSystemId(), pageSize, locator);
        ListPointTutorialResponse response = new ListPointTutorialResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setTutorials(tutorials.stream().map(this::toPointTutorialWithoutMappingDTO).collect(Collectors.toList()));
        return response;
    }

    private PointSystemDTO toPointSystemDTO(PointSystem pointSystem) {
        return ConvertHelper.convert(pointSystem, PointSystemDTO.class);
    }

    @Override
    public ListPointTutorialDetailResponse listPointTutorialDetail(ListPointTutorialDetailCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointTutorialToPointRuleMapping> mappings = pointTutorialToPointRuleMappingProvider.listMappings(cmd.getTutorialId(), pageSize, locator);
        List<PointTutorialDetailDTO> mappingDTOS = mappings.stream().map(this::toPointTutorialDetailDTO).collect(Collectors.toList());

        ListPointTutorialDetailResponse response = new ListPointTutorialDetailResponse();
        response.setDetails(mappingDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private PointTutorialDetailDTO toPointTutorialDetailDTO(PointTutorialToPointRuleMapping mapping) {
        PointTutorialDetailDTO detailDTO = ConvertHelper.convert(mapping, PointTutorialDetailDTO.class);
        PointRule pointRule = pointRuleProvider.findById(mapping.getRuleId());
        if (detailDTO.getDescription() == null || detailDTO.getDescription().trim().isEmpty()) {
            detailDTO.setDescription(pointRule.getDescription());
        }
        detailDTO.setRuleName(pointRule.getDisplayName());
        detailDTO.setPoints(pointRule.getPoints());

        PointRuleConfig ruleConfig = pointRuleConfigProvider.findByRuleIdAndSystemId(mapping.getSystemId(), mapping.getRuleId());
        if (ruleConfig != null) {
            detailDTO.setDescription(ruleConfig.getDescription());
            detailDTO.setPoints(ruleConfig.getPoints());
        }
        return detailDTO;
    }

    private PointTutorialToPointRuleMappingDTO toPointTutorialToPointRuleMappingDTO(PointTutorialToPointRuleMapping mapping) {
        return ConvertHelper.convert(mapping, PointTutorialToPointRuleMappingDTO.class);
    }

    private String parseURI(String uri, String ownerType, Long ownerId) {
        try {
            return contentServerService.parserUri(uri, ownerType, ownerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    public ListPointLogsResponse listPointLogsForMall(ListPointLogsForMallCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        ListPointLogsCommand logsCmd = ConvertHelper.convert(cmd, ListPointLogsCommand.class);
        logsCmd.setEventName(SystemEvent.BIZ_ORDER_CREATE.dft());

        List<PointLog> logs = pointLogProvider.listPointLogs(logsCmd, locator);
        List<PointLogDTO> dtoList = logs.stream().map(this::toPointLogDTO).collect(Collectors.toList());

        dtoList = dtoList.parallelStream().map(r -> {
            Map<String, Object> map = (Map) StringHelper.fromJsonString(r.getExtra(), Map.class);
            Object scoreObj = map.get("score");
            if (scoreObj == null) {
                return null;
            }
            r.setTargetPhone(null);

            PointSystem pointSystem = pointSystemProvider.findById(r.getSystemId());

            BigDecimal discountPrice = new BigDecimal(scoreObj.toString());
            BigDecimal exchangePoint = new BigDecimal(pointSystem.getExchangePoint());
            BigDecimal exchangeCash = new BigDecimal(pointSystem.getExchangeCash());

            BigDecimal discount = discountPrice.divide(exchangePoint, 2).multiply(exchangeCash);

            r.setDiscountPrice(discount.floatValue());
            r.setTargetName(maskTargetName(r.getTargetName()));
            return r;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        ListPointLogsResponse response = new ListPointLogsResponse();
        response.setLogs(dtoList);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private String maskTargetName(String targetName) {
        int length = targetName.length();
        if (length > 1) {
            int i = length / 2;

            int prefixBeginIndex = 0;
            int prefixEndIndex = i / 2 == 0 ? 1 : i / 2;
            int suffixBeginIndex = (length - prefixEndIndex) == 1 ? 2 : length - prefixEndIndex;

            targetName = targetName.substring(prefixBeginIndex, prefixEndIndex) + "**" + targetName.substring(suffixBeginIndex, length);
        }
        return targetName;
    }

    /*public static void main(String[] args) {
        String targetName = "12222222";
        int length = targetName.length();
        if (length > 1) {
            int i = length / 2;

            int prefixBeginIndex = 0;
            int prefixEndIndex = i / 2 == 0 ? 1 : i / 2;
            int suffixBeginIndex = (length - prefixEndIndex) == 1 ? 2 : length - prefixEndIndex;
            int suffixEndIndex = length;

            targetName = targetName.substring(prefixBeginIndex, prefixEndIndex) + "**" + targetName.substring(suffixBeginIndex, suffixEndIndex);
        }
        System.out.println(targetName);
    }*/

    @Override
    public PointSystemDTO createPointSystem(CreatePointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);
        checkPointSystemNameAndPointName(null, cmd.getNamespaceId(), cmd.getDisplayName(), cmd.getPointName());

        PointSystem pointSystem = new PointSystem();
        pointSystem.setDisplayName(cmd.getDisplayName().trim());
        pointSystem.setPointName(cmd.getPointName().trim());
        pointSystem.setNamespaceId(cmd.getNamespaceId());
        pointSystem.setPointExchangeFlag(TrueOrFalseFlag.TRUE.getCode());
        pointSystem.setExchangeCash(1);
        pointSystem.setExchangePoint(100);
        pointSystem.setUserAgreement("");
        pointSystem.setStatus(PointCommonStatus.DISABLED.getCode());

        dbProvider.execute(status -> {
            pointSystemProvider.createPointSystem(pointSystem);
            return true;
        });
        return toPointSystemDTO(pointSystem);
    }

    private void checkPointSystemNameAndPointName(Long systemId, Integer namespaceId, String systemName, String pointName) {
        List<PointSystem> pointSystems = pointSystemProvider.listPointSystems(namespaceId, -1, new ListingLocator());
        Map<String, Long> displayNames = pointSystems.parallelStream().collect(Collectors.toMap(PointSystem::getDisplayName, PointSystem::getId));
        if (systemName != null && displayNames.keySet().contains(systemName.trim()) && !Objects.equals(displayNames.get(systemName.trim()), systemId)) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NAME_EXIST_CODE,
                    "Point system name already exist");
        }

        Map<String, Long> pointNames = pointSystems.parallelStream().collect(Collectors.toMap(PointSystem::getPointName, PointSystem::getId));
        if (pointName != null && pointNames.keySet().contains(pointName.trim()) && !Objects.equals(pointNames.get(pointName.trim()), systemId)) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_NAME_EXIST_CODE,
                    "Point name already exist");
        }
    }

    @Override
    public PointSystemDTO updatePointSystem(UpdatePointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }
        checkPointSystemNameAndPointName(cmd.getId(), pointSystem.getNamespaceId(), cmd.getDisplayName(), cmd.getPointName());

        pointSystem.setDisplayName(cmd.getDisplayName());
        pointSystem.setPointName(cmd.getPointName());
        pointSystem.setExchangePoint(cmd.getExchangePoint());
        pointSystem.setExchangeCash(cmd.getExchangeCash());
        pointSystem.setPointExchangeFlag(cmd.getPointExchangeFlag());
        pointSystem.setUserAgreement(cmd.getUserAgreement());

        pointSystemProvider.updatePointSystem(pointSystem);
        return toPointSystemDTO(pointSystem);
    }

    @Override
    public ListPointRuleCategoriesResponse listPointRuleCategories() {
        List<PointRuleCategory> categories = pointRuleCategoryProvider.listPointRuleCategories();
        List<PointRuleCategoryDTO> categoryDTOS = categories.stream().map(this::toPointRuleCategoryDTO).collect(Collectors.toList());
        ListPointRuleCategoriesResponse response = new ListPointRuleCategoriesResponse();
        response.setCategories(categoryDTOS);
        return response;
    }

    @Override
    public PointLogDTO createPointLog(CreatePointLogCommand cmd) {
        ValidatorUtil.validate(cmd);

        Boolean pass = pictureValidateService.validateCode(cmd.getSessionId(), cmd.getCaptcha());
        if (!pass) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_CAPTCHA_VERIFY_FAILURE_CODE,
                    "Captcha verify failure");
        }

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        User user = userService.findUserByIndentifier(pointSystem.getNamespaceId(), cmd.getPhone());
        if (user == null) {
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user information");
        }

        PointLog pointLog = new PointLog();
        pointLog.setSystemId(cmd.getSystemId());
        pointLog.setPoints(cmd.getPoints());
        pointLog.setDescription(cmd.getDescription());
        pointLog.setNamespaceId(pointSystem.getNamespaceId());
        pointLog.setTargetName(user.getNickName());
        pointLog.setTargetUid(user.getId());
        pointLog.setTargetPhone(cmd.getPhone());
        pointLog.setArithmeticType(PointArithmeticType.ADD.getCode());
        pointLog.setEventHappenTime(System.currentTimeMillis());

        // 手动添加的归到账号里面
        PointRuleCategory category = pointRuleCategoryProvider.findById(1L);
        pointLog.setCategoryId(category.getId());
        pointLog.setCategoryName(category.getDisplayName());

        String operatorName = "unknown";
        String operatorPhone = "unknown";

        Long operatorUid = UserContext.currentUserId();
        UserInfo operator = userService.getUserSnapshotInfoWithPhone(operatorUid);
        if (operator != null) {
            operatorName = operator.getNickName();
            if (operator.getPhones() != null && operator.getPhones().size() > 0) {
                operatorPhone = operator.getPhones().iterator().next();
            } else {
                LOGGER.warn("User phones not found for uid = {}", operatorUid);
            }
        } else {
            LOGGER.warn("User not found for uid = {}", operatorUid);
        }

        pointLog.setOperatorUid(operatorUid);
        pointLog.setOperatorName(operatorName);
        pointLog.setOperatorPhone(operatorPhone);
        pointLog.setOperatorType(PointOperatorType.MANUALLY.getCode());

        dbProvider.execute(status -> {
            pointLogProvider.createPointLog(pointLog);

            PointScoreLockAndCacheKey lockKey = new PointScoreLockAndCacheKey(pointSystem.getNamespaceId(), pointSystem.getId(), user.getId());

            coordinationProvider.getNamedLock(CoordinationLocks.POINT_UPDATE_POINT_SCORE.getCode() + lockKey.toString()).enter(() -> {
                PointScore pointScore = pointScoreProvider.findUserPointScore(pointSystem.getNamespaceId(),
                        pointSystem.getId(), user.getId(), PointScore.class);
                if (pointScore != null) {
                    pointScore.setScore(pointScore.getScore() + cmd.getPoints());
                    pointScoreProvider.updatePointScore(pointScore);
                } else {
                    pointScore = new PointScore();
                    pointScore.setScore(pointLog.getPoints());
                    pointScore.setNamespaceId(pointSystem.getNamespaceId());
                    pointScore.setSystemId(pointSystem.getId());
                    pointScore.setUserId(user.getId());
                    pointScoreProvider.createPointScore(pointScore);
                }
                return true;
            });
            return true;
        });
        return toPointLogDTO(pointLog);
    }

    @Override
    public ListPointRulesResponse listPointRules(ListPointRulesCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointRuleDTO> pointRules = pointRuleProvider.listPointRules(cmd, pageSize, locator);
        ListPointRulesResponse response = new ListPointRulesResponse();
        response.setRules(pointRules);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public PointGoodDTO updatePointGood(UpdatePointGoodCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        boolean createFlag = false;
        PointGood good = pointGoodProvider.findBySystemAndGood(cmd.getSystemId(), cmd.getNumber(), cmd.getShopNumber());
        if (good == null) {
            good = new PointGood();
            good.setStatus(PointCommonStatus.DISABLED.getCode());
            good.setTopStatus(PointCommonStatus.DISABLED.getCode());
            good.setNamespaceId(pointSystem.getNamespaceId());
            good.setSystemId(pointSystem.getId());

            createFlag = true;
        }

        good.setShopNumber(cmd.getShopNumber());
        good.setNumber(cmd.getNumber());

        PointCommonStatus status = PointCommonStatus.fromCode(cmd.getStatus());
        if (status != null) {
            good.setStatus(status.getCode());
        }

        PointCommonStatus topStatus = PointCommonStatus.fromCode(cmd.getTopStatus());
        if (topStatus == PointCommonStatus.ENABLED) {
            good.setTopStatus(topStatus.getCode());
            good.setTopTime(DateUtils.currentTimestamp());
        } else if (topStatus == PointCommonStatus.DISABLED) {
            good.setTopStatus(topStatus.getCode());
            good.setTopTime(null);
        }

        if (createFlag) {
            pointGoodProvider.createPointGood(good);
        } else {
            pointGoodProvider.updatePointGood(good);
        }
        return new PointGoodDTO();
    }

    @Override
    public PointTutorialDTO deletePointTutorial(DeletePointTutorialCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointTutorial tutorial = pointTutorialProvider.findById(cmd.getTutorialId());
        if (tutorial == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_TUTORIAL_NOT_EXIST_CODE,
                    "Point tutorial not exist");
        }

        dbProvider.execute(status -> {
            pointTutorialProvider.deleteTutorial(tutorial);
            pointTutorialToPointRuleMappingProvider.deleteByTutorialId(tutorial.getId());
            return true;
        });
        return null;
    }

    @Override
    public PointTutorialDTO createOrUpdatePointTutorial(CreateOrUpdatePointTutorialCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointTutorial tutorial;
        if (cmd.getId() != null) {
            tutorial = pointTutorialProvider.findById(cmd.getId());
            if (tutorial == null) {
                throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_TUTORIAL_NOT_EXIST_CODE,
                        "Point tutorial not exist");
            }

            tutorial.setDisplayName(cmd.getDisplayName());
            tutorial.setDescription(cmd.getDescription());
            tutorial.setPosterUri(cmd.getPosterUri());

            dbProvider.execute(status -> {
                pointTutorialProvider.updatePointTutorial(tutorial);
                pointTutorialToPointRuleMappingProvider.deleteByTutorialId(tutorial.getId());
                createTutorialToPointRuleMapping(cmd.getMappings(), pointSystem, tutorial);
                return true;
            });
        } else {
            tutorial = ConvertHelper.convert(cmd, PointTutorial.class);
            tutorial.setSystemId(pointSystem.getId());
            tutorial.setNamespaceId(pointSystem.getNamespaceId());

            dbProvider.execute(status -> {
                pointTutorialProvider.createPointTutorial(tutorial);
                createTutorialToPointRuleMapping(cmd.getMappings(), pointSystem, tutorial);
                return true;
            });
        }
        return toPointTutorialWithMappingDTO(tutorial);
    }

    @Override
    public ListPointTutorialResponse listPointTutorialsWithMapping(ListPointTutorialsCommand cmd) {
        ValidatorUtil.validate(cmd);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointTutorial> tutorials = pointTutorialProvider.listPointTutorials(cmd.getSystemId(), pageSize, locator);

        List<PointTutorialDTO> tutorialDTOS = new ArrayList<>();
        for (PointTutorial tutorial : tutorials) {
            tutorialDTOS.add(toPointTutorialWithMappingDTO(tutorial));
        }

        ListPointTutorialResponse response = new ListPointTutorialResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setTutorials(tutorialDTOS);
        return response;
    }

    private PointTutorialDTO toPointTutorialWithoutMappingDTO(PointTutorial pointTutorial) {
        PointTutorialDTO dto = ConvertHelper.convert(pointTutorial, PointTutorialDTO.class);
        dto.setPosterUrl(parseURI(pointTutorial.getPosterUri(), EhPointTutorials.class.getSimpleName(), pointTutorial.getId()));
        return dto;
    }

    private PointTutorialDTO toPointTutorialWithMappingDTO(PointTutorial tutorial) {
        PointTutorialDTO tutorialDTO = toPointTutorialWithoutMappingDTO(tutorial);
        List<PointTutorialToPointRuleMapping> mappings = pointTutorialToPointRuleMappingProvider.listMappings(
                tutorial.getId(), -1, new ListingLocator());
        List<PointTutorialToPointRuleMappingDTO> mappingDTOList = mappings.stream()
                .map(this::toPointTutorialToPointRuleMappingDTO).collect(Collectors.toList());
        tutorialDTO.setMappings(mappingDTOList);
        return tutorialDTO;
    }

    private void createTutorialToPointRuleMapping(List<PointTutorialMappingCommand> cmds, PointSystem pointSystem, PointTutorial tutorial) {
        if (cmds != null) {
            for (PointTutorialMappingCommand mappingCmd : cmds) {
                PointTutorialToPointRuleMapping mapping = new PointTutorialToPointRuleMapping();
                mapping.setRuleId(mappingCmd.getRuleId());
                mapping.setSystemId(pointSystem.getId());
                mapping.setTutorialId(tutorial.getId());
                mapping.setNamespaceId(pointSystem.getNamespaceId());
                mapping.setDescription(mappingCmd.getDescription());

                pointTutorialToPointRuleMappingProvider.createPointTutorialToPointRuleMapping(mapping);
            }
        }
    }

    @Override
    public ListPointLogsResponse listManuallyPointLogs(ListPointLogsCommand cmd) {
        cmd.setOperatorType(PointOperatorType.MANUALLY.getCode());
        return this.listPointLogs(cmd);
    }

    @Override
    public PointBannerDTO createPointBanner(CreatePointBannerCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getSystemId());
        if (pointSystem == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        PointBanner banner = ConvertHelper.convert(cmd, PointBanner.class);
        Integer maxDefaultOrder = pointBannerProvider.findMaxDefaultOrder(cmd.getSystemId());
        if (maxDefaultOrder == null) {
            maxDefaultOrder = 0;
        }
        banner.setStatus(PointCommonStatus.ENABLED.getCode());
        banner.setNamespaceId(pointSystem.getNamespaceId());
        banner.setDefaultOrder(++maxDefaultOrder);

        pointBannerProvider.createPointBanner(banner);

        return toPointBannerDTO(banner);
    }

    private PointBannerDTO toPointBannerDTO(PointBanner banner) {
        PointBannerDTO dto = ConvertHelper.convert(banner, PointBannerDTO.class);
        dto.setPosterUrl(parseURI(banner.getPosterUri(), EhPointBanners.class.getSimpleName(), banner.getId()));
        return dto;
    }

    @Override
    public PointBannerDTO updatePointBanner(UpdatePointBannerCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointBanner banner = pointBannerProvider.findById(cmd.getId());
        if (banner == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_BANNER_NOT_EXIST_CODE,
                    "Point banner not exist");
        }

        banner.setName(cmd.getName());
        banner.setActionType(cmd.getActionType());
        banner.setActionData(cmd.getActionData());
        banner.setPosterUri(cmd.getPosterUri());

        pointBannerProvider.updatePointBanner(banner);
        return toPointBannerDTO(banner);
    }

    @Override
    public ListPointBannersResponse listPointBanners(ListPointBannersCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointBanner> banners = pointBannerProvider.listPointBannersBySystemId(
                cmd.getSystemId(), cmd.getStatus(), pageSize, locator);
        List<PointBannerDTO> dtoList = banners.stream().map(this::toPointBannerDTO).collect(Collectors.toList());

        ListPointBannersResponse response = new ListPointBannersResponse();
        response.setBanners(dtoList);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public void deletePointBanner(DeletePointBannerCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointBanner banner = pointBannerProvider.findById(cmd.getId());
        if (banner == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_BANNER_NOT_EXIST_CODE,
                    "Point banner not exist");
        }
        pointBannerProvider.deletePointBanner(banner);
    }

    @Override
    public void reorderPointBanners(ReorderPointBannersCommand cmd) {
        ValidatorUtil.validate(cmd);

        Map<Long, Integer> idToOrderMap = cmd.getOrders().stream().collect(Collectors.toMap(PointBannerOrder::getId, PointBannerOrder::getDefaultOrder));

        List<PointBanner> pointBanners = pointBannerProvider.listByIds(idToOrderMap.keySet());
        for (PointBanner banner : pointBanners) {
            banner.setDefaultOrder(idToOrderMap.get(banner.getId()));
            pointBannerProvider.updatePointBanner(banner);
        }
    }

    @Override
    public PointBannerDTO updatePointBannerStatus(UpdatePointBannerStatusCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointBanner banner = pointBannerProvider.findById(cmd.getId());
        if (banner == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_BANNER_NOT_EXIST_CODE,
                    "Point banner not exist");
        }
        if (!Objects.equals(banner.getStatus(), cmd.getStatus())) {
            banner.setStatus(cmd.getStatus());
            pointBannerProvider.updatePointBanner(banner);
        }
        return toPointBannerDTO(banner);
    }

    @Override
    public PublishEventResultDTO publishEvent(PublishEventCommand cmd) {
        ValidatorUtil.validate(cmd);
        boolean result = pointServiceRPCRest.publishPointCostEvent(cmd);
        if(!result){
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "event handle failure  .");
        }
        /*LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(cmd.getEventJson(), LocalEvent.class);
        LocalEventBus.publish(localEvent);*/
        return null;
    }

    private PointRuleDTO toPointRuleDTO(PointRule pointRule) {
        PointRuleDTO dto = ConvertHelper.convert(pointRule, PointRuleDTO.class);
        PointRuleCategory category = pointRuleCategoryProvider.findById(pointRule.getCategoryId());
        if (category != null) {
            dto.setCategoryName(category.getDisplayName());
        }
        return dto;
    }

    private PointRuleCategoryDTO toPointRuleCategoryDTO(PointRuleCategory pointRuleCategory) {
        return ConvertHelper.convert(pointRuleCategory, PointRuleCategoryDTO.class);
    }

    // 一年执行一次发送积分清零消息
    @Scheduled(cron = "0 0 18 24 12 ?")
    @Override
    public void everyYearEndSendMessageSchedule() {
        if (scheduleProvider.getRunningFlag() == RunningFlag.FALSE.getCode()) {
            return;
        }
        List<PointSystem> systemList = pointSystemProvider.getEnabledPointSystems(null);

        for (PointSystem system : systemList) {
            TrueOrFalseFlag flag = TrueOrFalseFlag.fromCode(system.getPointExchangeFlag());
            if (flag == TrueOrFalseFlag.FALSE) {
                continue;
            }

            List<PointScore> pointScores = pointScoreProvider.listPointScoreBySystem(system.getId());
            for (PointScore score : pointScores) {
                if (score.getScore() <= 0) {
                    continue;
                }

                try {
                    // Map<String, String> model = new HashMap<>();
                    // model.put("points", defaultIfNull(score.getScore(), "0"));

                    String template = localeTemplateService.getLocaleTemplateString(
                            PointTemplateCode.SCOPE,
                            PointTemplateCode.POINT_WILL_EXPIRED_CODE,
                            currentLocale(),
                            null,
                            "Template Not Found"
                    );

                    PointGeneralTemplate genTpl = getGeneralTemplate();

                    MessageDTO messageDto = new MessageDTO();
                    messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                    messageDto.setBody(template);
                    messageDto.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(score.getUserId())));
                    // 组装路由
                    OfficialActionData actionData = new OfficialActionData();
                    actionData.setUrl(getPointSystemUrl(system.getId()));
                    // 拼装路由参数
                    String url = RouterBuilder.build(Router.BROWSER_I, actionData);
                    RouterMetaObject metaObject = new RouterMetaObject();
                    metaObject.setUrl(url);
                    // 组装消息 meta
                    Map<String, String> meta = new HashMap<>();
                    meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
                    meta.put(MessageMetaConstant.MESSAGE_SUBJECT, genTpl.getMessageTitle());
                    meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
                    messageDto.setMeta(meta);

                    messagingService.routeMessage(
                            User.SYSTEM_USER_LOGIN,
                            AppConstants.APPID_MESSAGING,
                            MessageChannelType.USER.getCode(),
                            String.valueOf(score.getUserId()),
                            messageDto,
                            MessagingConstants.MSG_FLAG_STORED.getCode()
                    );
                } catch (Exception e) {
                    LOGGER.error("Point message error, score = " + score.toString(), e);
                }
            }
        }
    }

    @Override
    public PointGeneralTemplate getGeneralTemplate() {
        String generalTemplate = localeTemplateService.getLocaleTemplateString(
                PointTemplateCode.SCOPE,
                PointTemplateCode.POINT_GENERAL_TEMPLATE_CODE,
                currentLocale(),
                null,
                ""
        );
        return (PointGeneralTemplate) StringHelper.fromJsonString(generalTemplate, PointGeneralTemplate.class);
    }

    private String currentLocale() {
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        User user = UserContext.current().getUser();
        if (user != null) {
            locale = user.getLocale();
        }
        return locale;
    }

    // 一年执行一次积分清零操作
    @Scheduled(cron = "0 0 0 31 12 ?")
    @Override
    public void everyYearEndClearPointSchedule() {
        if (scheduleProvider.getRunningFlag() == RunningFlag.FALSE.getCode()) {
            return;
        }
        List<PointSystem> systemList = pointSystemProvider.listPointSystems();
        for (PointSystem system : systemList) {
            List<PointScore> pointScores = pointScoreProvider.listPointScoreBySystem(system.getId());
            for (PointScore tempScore : pointScores) {
                try {
                    if (tempScore.getScore() == 0) {
                        continue;
                    }

                    PointScoreLockAndCacheKey lockKey = new PointScoreLockAndCacheKey(system.getNamespaceId(), system.getId(), tempScore.getUserId());
                    coordinationProvider.getNamedLock(
                            CoordinationLocks.POINT_UPDATE_POINT_SCORE.getCode() + lockKey.toString()).enter(() -> {

                        PointScore score = pointScoreProvider.findUserPointScore(system.getNamespaceId(), system.getId(), tempScore.getUserId(), PointScore.class);

                        PointLog pointLog = new PointLog();
                        if (score.getScore() > 0) {
                            pointLog.setArithmeticType(PointArithmeticType.SUBTRACT.getCode());
                            pointLog.setPoints(score.getScore());
                        } else {
                            pointLog.setArithmeticType(PointArithmeticType.ADD.getCode());
                            pointLog.setPoints(-score.getScore());
                        }

                        PointGeneralTemplate genTpl = getGeneralTemplate();
                        pointLog.setOperatorType(PointOperatorType.SYSTEM.getCode());
                        pointLog.setTargetUid(score.getUserId());
                        pointLog.setNamespaceId(score.getNamespaceId());
                        pointLog.setSystemId(system.getId());
                        pointLog.setEventName("point.reset_point_score");
                        pointLog.setEntityType(EhPointScores.class.getSimpleName());
                        pointLog.setEntityId(score.getId());
                        pointLog.setDescription(genTpl.getResetPointDesc());

                        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(score.getUserId());
                        pointLog.setTargetPhone(userInfo.getPhones().get(0));
                        pointLog.setTargetName(userInfo.getNickName());
                        pointLog.setRuleId(0L);
                        pointLog.setRuleName(genTpl.getResetPointDesc());
                        pointLog.setCategoryId(0L);
                        pointLog.setCategoryName(genTpl.getResetPointCate());
                        pointLog.setEventHappenTime(System.currentTimeMillis());

                        pointLogProvider.createPointLog(pointLog);

                        score.setScore(0L);
                        pointScoreProvider.updatePointScore(score);
                        return true;
                    });
                } catch (Exception e) {
                    LOGGER.error("Point reset error, score = " + tempScore.toString(), e);
                }
            }
        }
    }

    @Override
    public void exportPointLog(ExportPointLogsCommand cmd, HttpServletResponse response) {
        ListPointLogsCommand listCmd = ConvertHelper.convert(cmd, ListPointLogsCommand.class);
        listCmd.setPageSize(1000);
        ListPointLogsResponse pointLogs = this.listPointLogs(listCmd);

        PointGeneralTemplate template = getGeneralTemplate();

        String fileName = buildPointLogFileName(cmd, template);

        ExcelUtils excelUtils = new ExcelUtils(response, fileName, "Sheet-1");

        String[] propertyNames = {"targetName", "targetPhone", "categoryName", "description", "points", "createTime"};
        String[] titleNames = template.getExportLogTitle().split(",");
        int[] columnSizes = {20, 30, 20, 40, 20, 30};

        excelUtils.writeExcel(propertyNames, titleNames, columnSizes, toExportPointLogDTOList(pointLogs.getLogs()));
    }

    private String buildPointLogFileName(ExportPointLogsCommand cmd, PointGeneralTemplate template) {
        String fileName = template.getExportLogFileName();
        if (cmd.getStartTime() != null) {
            fileName += "_";
            fileName += new Timestamp(cmd.getStartTime()).toLocalDateTime()
                    .toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        if (cmd.getEndTime() != null) {
            fileName += (cmd.getStartTime() != null ? "~" : "_");
            fileName += new Timestamp(cmd.getEndTime()).toLocalDateTime()
                    .toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        if (StringUtils.isNotEmpty(cmd.getPhone())) {
            fileName += "_";
            fileName += cmd.getPhone().trim();
        }
        return fileName;
    }

    private List<ExportPointLogVO> toExportPointLogDTOList(List<PointLogDTO> logs) {
        List<ExportPointLogVO> vos = new ArrayList<>();
        for (PointLogDTO log : logs) {
            ExportPointLogVO vo = new ExportPointLogVO();
            vo.setCategoryName(log.getCategoryName());
            vo.setDescription(log.getDescription());
            vo.setTargetName(log.getTargetName());
            vo.setTargetPhone(log.getTargetPhone());

            vo.setCreateTime(log.getCreateTime().toLocalDateTime().toLocalDate().toString());

            String prefix = "";
            if (Objects.equals(log.getArithmeticType(), PointArithmeticType.ADD.getCode())) {
                prefix = "+";
            } else if (Objects.equals(log.getArithmeticType(), PointArithmeticType.SUBTRACT.getCode())) {
                prefix = "-";
            }
            vo.setPoints(prefix + log.getPoints());
            vos.add(vo);
        }
        return vos;
    }
}