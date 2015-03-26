// @formatter:off
package com.everhomes.region.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.RequireAuthentication;
import com.everhomes.discover.RestReturn;
import com.everhomes.region.ListRegionCommand;
import com.everhomes.region.Region;
import com.everhomes.region.RegionAdminStatus;
import com.everhomes.region.RegionDTO;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

/**
 * Region REST API controller
 * 
 * @author Kelven Yang
 *
 */
@RestController
@RequestMapping("/region")
public class RegionController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionProvider regionProvider;
    
    @RequireAuthentication(false)
    @RequestMapping("list")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse list(@Valid ListRegionCommand cmd) {
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("API request /region/list: " + cmd.toString());

        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listRegions(RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
    
    @RequireAuthentication(false)
    @RequestMapping("listChildren")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listChildren(@Valid ListRegionCommand cmd) {
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("API request /region/listChildren: " + cmd.toString());

        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listChildRegions(cmd.getParentId(), 
            RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
    
    @RequireAuthentication(false)
    @RequestMapping("listDescendants")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listDescendants(@Valid ListRegionCommand cmd) {
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("API request /region/listDescendants: " + cmd.toString());

        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listDescendantRegions(cmd.getParentId(), 
            RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
}
