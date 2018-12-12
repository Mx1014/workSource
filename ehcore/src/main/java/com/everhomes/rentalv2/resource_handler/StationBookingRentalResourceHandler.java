package com.everhomes.rentalv2.resource_handler;

import com.everhomes.officecubicle.OfficeCubicleProvider;
import com.everhomes.officecubicle.OfficeCubicleSpace;
import com.everhomes.parking.ParkingLot;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.admin.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "station_booking")
public class StationBookingRentalResourceHandler implements RentalResourceHandler {
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private OfficeCubicleProvider officeCubicleProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private Rentalv2Service rentalService;
    @Override
    public RentalResource getRentalResourceById(Long id) {
    	OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(id);
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(space.getNamespaceId(),
                RentalV2ResourceType.STATION_BOOKING.getCode());

        RentalResource resource = new RentalResource();
        resource.setId(space.getId());
        resource.setResourceName(space.getName());
        resource.setResourceType(RentalV2ResourceType.STATION_BOOKING.getCode());
        resource.setResourceTypeId(type.getId());
        Integer shortRentNums = 0;
        if (space.getShortRentNums() != null) {
        	shortRentNums = Integer.valueOf(space.getShortRentNums());
        }
        resource.setResourceCounts(Double.valueOf(shortRentNums));
        resource.setAutoAssign(NormalFlag.NONEED.getCode());
        resource.setMultiUnit(NormalFlag.NONEED.getCode());
        resource.setCommunityId(space.getOwnerId());

        return resource;
    }

    @Override
    public void updateRentalResource(String resourceJson) {

    }

    @Override
    public void buildDefaultRule(AddDefaultRuleAdminCommand addCmd) {
        List<Byte> rentalTypes = Arrays.asList(RentalType.HALFDAY.getCode(), RentalType.DAY.getCode());
        addCmd.setPriceRules(rentalCommonService.buildDefaultPriceRule(rentalTypes));
        addCmd.setRentalTypes(rentalTypes);
        //设置按半天模式 开放时间
        List<TimeIntervalDTO> timeIntervals = new ArrayList<>();
        TimeIntervalDTO timeIntervalDTO = new TimeIntervalDTO();
        timeIntervalDTO.setBeginTime(8D);
        timeIntervalDTO.setEndTime(12D);
        timeIntervalDTO.setAmorpm((byte)0);
        timeIntervalDTO.setName("上午");
        timeIntervals.add(timeIntervalDTO);
        timeIntervalDTO = new TimeIntervalDTO();
        timeIntervalDTO.setBeginTime(12D);
        timeIntervalDTO.setEndTime(18D);
        timeIntervalDTO.setAmorpm((byte)1);
        timeIntervalDTO.setName("下午");
        timeIntervals.add(timeIntervalDTO);
        addCmd.setHalfDayTimeIntervals(timeIntervals);

        addCmd.setTimeIntervals(Collections.singletonList(timeIntervalDTO));
        //设置按天模式 开放时间
        List<RentalOpenTimeDTO> openTimes = new ArrayList<>();
        RentalOpenTimeDTO openTimeDTO = new RentalOpenTimeDTO();
        openTimeDTO.setRentalType(RentalType.DAY.getCode());
        openTimeDTO.setDayOpenTime(8D);
        openTimeDTO.setDayCloseTime(22D);
        openTimes.add(openTimeDTO);
        addCmd.setOpenTimes(openTimes);

        //默认关闭节假日
        addCmd.setHolidayOpenFlag((byte)0);
        addCmd.setHolidayType(RentalHolidayType.NORMAL_WEEKEND.getCode());

        //填写关闭日期
        GetHolidayCloseDatesCommand cmd = new GetHolidayCloseDatesCommand();
        cmd.setHolidayType(RentalHolidayType.NORMAL_WEEKEND.getCode());
        addCmd.setCloseDates(rentalService.getHolidayCloseDates(cmd));
    }

    @Override
    public void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource) {
        queryRuleCmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
        queryRuleCmd.setOwnerId(resource.getCommunityId());
    }
}
