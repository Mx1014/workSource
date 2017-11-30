// @formatter:off
package com.everhomes.point;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.point.*;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.server.schema.tables.pojos.EhPointGoods;
import com.everhomes.server.schema.tables.pojos.EhPointTutorials;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public GetEnabledPointSystemResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd) {
        ValidatorUtil.validate(cmd);
        List<PointSystem> systems = pointSystemProvider.getEnabledPointSystems(cmd.getNamespaceId());
        List<PointSystemDTO> dtoList = systems.stream().map(this::toPointSystemDTO).collect(Collectors.toList());
        return new GetEnabledPointSystemResponse(dtoList);
    }

    @Override
    public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {
        // ValidatorUtil.validate(cmd);
        //
        // Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();
        // Long uid = cmd.getUid() != null ? cmd.getUid() : UserContext.currentUserId();
        //
        // return pointScoreProvider.findUserPointScore(namespaceId, cmd.getSystemId(), uid, PointScoreDTO.class);

        PointScoreDTO dto = new PointScoreDTO();
        dto.setScore(UserContext.currentUserId());
        return dto;
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
        cmd.setUserId(null);
        // -------

        List<PointLog> logs = pointLogProvider.listPointLogs(cmd, locator);

        ListPointLogsResponse response = new ListPointLogsResponse();

        List<PointLogDTO> logDTOS = logs.stream().map(this::toPointLogDTO).collect(Collectors.toList());

        response.setLogs(logDTOS);
        response.setNextPageAnchor(locator.getAnchor());

        return response;
    }

    private PointLogDTO toPointLogDTO(PointLog pointLog) {
        return ConvertHelper.convert(pointLog, PointLogDTO.class);
    }

    @Override
    public ListPointGoodsResponse listPointGoods(ListPointGoodsCommand cmd) {
        ListPointGoodsResponse response = new ListPointGoodsResponse();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<PointGood> goods = pointGoodProvider.listPointGood(UserContext.getCurrentNamespaceId(), cmd.getSystemId(), pageSize, locator);

        List<PointGoodDTO> goodDTOS = goods.stream().map(this::toPointGoodDTO).collect(Collectors.toList());
        response.setGoods(goodDTOS);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private PointGoodDTO toPointGoodDTO(PointGood pointGood) {
        PointGoodDTO dto = ConvertHelper.convert(pointGood, PointGoodDTO.class);
        dto.setPosterUrl(contentServerService.parserUri(pointGood.getPosterUri(), EhPointGoods.class.getSimpleName(), pointGood.getId()));
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
        response.setTutorials(tutorials.stream().map(this::toTutorialDTO).collect(Collectors.toList()));

        return response;
    }

    private PointTutorialDTO toTutorialDTO(PointTutorial pointTutorial) {
        PointTutorialDTO dto = ConvertHelper.convert(pointTutorial, PointTutorialDTO.class);
        dto.setPosterUrl(contentServerService.parserUri(pointTutorial.getPosterUri(), EhPointTutorials.class.getSimpleName(), pointTutorial.getId()));
        return dto;
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

    @Override
    public ListPointMallBannersResponse listPointMallBanners(ListPointMallBannersCommand cmd) {
        ListPointMallBannersResponse response = new ListPointMallBannersResponse();

        String uri = "cs://1/image/aW1hZ2UvTVRwaE1qbGlZelV3TXpGbFltUTVZV1F6TnpOa1l6YzBNbUkzWkRreU5qZGpaUQ";
        String posterUrl = contentServerService.parserUri(uri, EhBanners.class.getSimpleName(), 1L);

        List<BannerDTO> banners = new ArrayList<>();
        BannerDTO dto = new BannerDTO();
        dto.setPosterUrl(posterUrl);
        dto.setActionType(ActionType.OFFICIAL_URL.getCode());

        OfficialActionData actionData = new OfficialActionData();
        actionData.setUrl("http://www.zuolin.com");

        dto.setActionData(StringHelper.toJsonString(actionData));

        banners.add(dto);

        dto = new BannerDTO();
        dto.setPosterUrl(posterUrl);
        dto.setActionType(ActionType.NONE.getCode());

        response.setBanners(banners);
        return response;
    }

    @Override
    public ListPointLogsResponse listPointLogsForMall(ListPointLogsForMallCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        ListPointLogsCommand logsCmd = ConvertHelper.convert(cmd, ListPointLogsCommand.class);

        List<PointLog> logs = pointLogProvider.listPointLogs(logsCmd, locator);
        ListPointLogsResponse response = new ListPointLogsResponse();
        response.setLogs(logs.stream().map(this::toPointLogDTO).collect(Collectors.toList()));
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }
}