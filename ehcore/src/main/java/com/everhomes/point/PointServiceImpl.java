// @formatter:off
package com.everhomes.point;

import com.everhomes.banner.Banner;
import com.everhomes.banner.BannerProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.point.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.server.schema.tables.pojos.EhPointGoods;
import com.everhomes.server.schema.tables.pojos.EhPointTutorials;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private BannerProvider bannerProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private PointRuleCategoryProvider pointRuleCategoryProvider;

    @Autowired
    private PointActionProvider pointActionProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PointEventLogScheduler pointEventLogScheduler;

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
            pointSystemProvider.updatePointSystem(pointSystem);
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
        pointEventLogScheduler.initScheduledTask();
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

        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();
        Long uid = cmd.getUid() != null ? cmd.getUid() : UserContext.currentUserId();

        return pointScoreProvider.findUserPointScore(namespaceId, cmd.getSystemId(), uid, PointScoreDTO.class);

        // PointScoreDTO dto = new PointScoreDTO();
        // dto.setScore(UserContext.currentUserId());
        // return dto;
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

        // --------
        // cmd.setUserId(null);
        // -------

        List<PointLog> logs = pointLogProvider.listPointLogs(cmd, locator);
        List<PointLogDTO> logDTOS = logs.stream().map(this::toPointLogDTO).collect(Collectors.toList());

        ListPointLogsResponse response = new ListPointLogsResponse();
        response.setLogs(logDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private PointLogDTO toPointLogDTO(PointLog pointLog) {
        return ConvertHelper.convert(pointLog, PointLogDTO.class);
    }

    @Override
    public ListPointGoodsResponse listPointGoods(ListPointGoodsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointGood> goods = pointGoodProvider.listPointGood(UserContext.getCurrentNamespaceId(), cmd, pageSize, locator);
        List<PointGoodDTO> goodDTOS = goods.stream().map(this::toPointGoodDTO).collect(Collectors.toList());

        ListPointGoodsResponse response = new ListPointGoodsResponse();
        response.setGoods(goodDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private PointGoodDTO toPointGoodDTO(PointGood pointGood) {
        PointGoodDTO dto = ConvertHelper.convert(pointGood, PointGoodDTO.class);
        dto.setPosterUrl(parseURI(pointGood.getPosterUri(),EhPointGoods.class.getSimpleName(), pointGood.getId()));
        return dto;
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
        detailDTO.setRuleName(pointRule.getDisplayName());
        detailDTO.setPoints(pointRule.getPoints());
        return detailDTO;
    }

    private PointTutorialToPointRuleMappingDTO toPointTutorialToPointRuleMappingDTO(PointTutorialToPointRuleMapping mapping) {
        return ConvertHelper.convert(mapping, PointTutorialToPointRuleMappingDTO.class);
    }

    @Override
    public ListPointMallBannersResponse listPointMallBanners(ListPointMallBannersCommand cmd) {
        ValidatorUtil.validate(cmd);

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<Banner> banners = bannerProvider.findBannersByTagAndScope(namespaceId, null,
                "/PointMall", "Default", (byte) 0, 0L);
        List<BannerDTO> dtoList = banners.stream().map(this::toBannerDTO).collect(Collectors.toList());

        ListPointMallBannersResponse response = new ListPointMallBannersResponse();
        response.setBanners(dtoList);
        return response;
    }

    private BannerDTO toBannerDTO(Banner banner) {
        BannerDTO dto = new BannerDTO();
        dto.setPosterUrl(parseURI(banner.getPosterPath(), EhBanners.class.getSimpleName(), banner.getId()));
        dto.setActionType(banner.getActionType());
        dto.setActionData(banner.getActionData());
        return dto;
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

        List<PointLog> logs = pointLogProvider.listPointLogs(logsCmd, locator);
        List<PointLogDTO> dtoList = logs.stream().map(this::toPointLogDTO).collect(Collectors.toList());

        ListPointLogsResponse response = new ListPointLogsResponse();
        response.setLogs(dtoList);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public PointSystemDTO createPointSystem(CreatePointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = ConvertHelper.convert(cmd, PointSystem.class);
        pointSystem.setNamespaceId(UserContext.getCurrentNamespaceId());
        pointSystem.setPointExchangeFlag(TrueOrFalseFlag.FALSE.getCode());
        pointSystem.setExchangeCash(1);
        pointSystem.setExchangePoint(100);
        pointSystem.setUserAgreement("");
        pointSystem.setStatus(PointCommonStatus.DISABLED.getCode());

        dbProvider.execute(status -> {
            pointSystemProvider.createPointSystem(pointSystem);
            doSnapshotPointRule(pointSystem);
            return true;
        });
        return toPointSystemDTO(pointSystem);
    }

    @Override
    public PointSystemDTO updatePointSystem(UpdatePointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointSystem pointSystem = pointSystemProvider.findById(cmd.getId());
        if (pointSystem == null) {
            throw RuntimeErrorException.errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_SYSTEM_NOT_EXIST_CODE,
                    "Point system not exist");
        }

        if (cmd.getDisplayName() != null) {
            pointSystem.setDisplayName(cmd.getDisplayName());
        }
        if (cmd.getPointName() != null) {
            pointSystem.setPointName(cmd.getPointName());
        }
        if (cmd.getExchangePoint() != null) {
            pointSystem.setExchangePoint(cmd.getExchangePoint());
        }
        if (cmd.getExchangeCash() != null) {
            pointSystem.setExchangeCash(cmd.getExchangeCash());
        }
        if (cmd.getPointExchangeFlag() != null) {
            if (!Objects.equals(cmd.getPointExchangeFlag(), pointSystem.getPointExchangeFlag())) {
                // TODO notify biz server
            }
            pointSystem.setPointExchangeFlag(cmd.getPointExchangeFlag());
        }
        if (cmd.getUserAgreement() != null) {
            cmd.setUserAgreement(cmd.getDisplayName());
        }
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

        Long operatorUid = UserContext.currentUserId();
        UserInfo operator = userService.getUserSnapshotInfoWithPhone(operatorUid);

        pointLog.setOperatorUid(operatorUid);
        pointLog.setOperatorName(operator.getNickName());
        pointLog.setOperatorPhone(operator.getPhones().get(0));
        pointLog.setOperatorType(PointOperatorType.MANUALLY.getCode());

        dbProvider.execute(status -> {
            pointLogProvider.createPointLog(pointLog);

            PointScore pointScore = pointScoreProvider.findUserPointScore(pointSystem.getNamespaceId(),
                    pointSystem.getId(), user.getId(), PointScore.class);
            if (pointScore != null) {
                pointScore.setScore(pointScore.getScore() + cmd.getPoints());
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
        return toPointLogDTO(pointLog);
    }

    @Override
    public ListPointRulesResponse listPointRules(ListPointRulesCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PointRule> pointRules = pointRuleProvider.listPointRules(cmd, pageSize, locator);
        List<PointRuleDTO> pointRuleDTOS = pointRules.stream().map(this::toPointRuleDTO).collect(Collectors.toList());

        ListPointRulesResponse response = new ListPointRulesResponse();
        response.setRules(pointRuleDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public PointGoodDTO updatePointGood(UpdatePointGoodCommand cmd) {
        ValidatorUtil.validate(cmd);

        PointGood good = pointGoodProvider.findById(cmd.getId());
        if (good == null) {
            throw errorWith(PointServiceErrorCode.SCOPE, PointServiceErrorCode.ERROR_POINT_GOOD_NOT_EXIST_CODE,
                    "Point good not exist");
        }

        PointCommonStatus status = PointCommonStatus.fromCode(cmd.getStatus());
        if (status != null) {
            good.setStatus(status.getCode());
        }

        TrueOrFalseFlag topStatus = TrueOrFalseFlag.fromCode(cmd.getTopStatus());
        if (topStatus != null) {
            good.setTopStatus(topStatus.getCode());
            good.setTopTime(DateUtils.currentTimestamp());
        }
        pointGoodProvider.updatePointGood(good);
        return toPointGoodDTO(good);
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
        dto.setPosterUrl(parseURI(pointTutorial.getPosterUri(),EhPointTutorials.class.getSimpleName(), pointTutorial.getId()));
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
            for (PointTutorialMappingCommand map : cmds) {
                PointTutorialToPointRuleMapping mapping = ConvertHelper.convert(map, PointTutorialToPointRuleMapping.class);
                mapping.setSystemId(pointSystem.getId());
                mapping.setTutorialId(tutorial.getId());
                mapping.setNamespaceId(pointSystem.getNamespaceId());
                pointTutorialToPointRuleMappingProvider.createPointTutorialToPointRuleMapping(mapping);
            }
        }
    }

    private PointRuleDTO toPointRuleDTO(PointRule pointRule) {
        return ConvertHelper.convert(pointRule, PointRuleDTO.class);
    }

    private PointRuleCategoryDTO toPointRuleCategoryDTO(PointRuleCategory pointRuleCategory) {
        return ConvertHelper.convert(pointRuleCategory, PointRuleCategoryDTO.class);
    }

    private void doSnapshotPointRule(PointSystem pointSystem) {
        List<PointRule> pointRules = pointRuleProvider.listPointRuleBySystemId(
                PointConstant.CONFIG_POINT_SYSTEM_ID, -1, new ListingLocator());
        pointRules.forEach(r -> {
            r.setSystemId(pointSystem.getId());
            r.setNamespaceId(pointSystem.getNamespaceId());
            r.setId(null);
        });
        pointRuleProvider.createPointRules(pointRules);

        List<PointAction> pointActions = pointActionProvider.listPointActionsBySystemId(PointConstant.CONFIG_POINT_SYSTEM_ID);
        pointActions.forEach(r -> {
            r.setSystemId(pointSystem.getId());
            r.setNamespaceId(pointSystem.getNamespaceId());
            r.setId(null);
        });
        for (PointAction action : pointActions) {

        }
    }

    @Override
    public void exportPointLog(ExportPointLogsCommand cmd, HttpServletResponse response) {

    }
}