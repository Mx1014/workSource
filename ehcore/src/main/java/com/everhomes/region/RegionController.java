// @formatter:off
package com.everhomes.region;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.region.ListActiveRegionCommand;
import com.everhomes.rest.region.ListRegionByKeywordCommand;
import com.everhomes.rest.region.ListRegionCommand;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.everhomes.util.RequireAuthentication;

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
    
    /**
     * <b>URL: /region/list</b>
     * 列出指定范围和状态的区域列表（不用填父亲区域ID）
     */
    @RequireAuthentication(false)
    @RequestMapping("list")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse list(@Valid ListRegionCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        if(dtoResultList != null){
            int hashCode = dtoResultList.hashCode();
            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
                return new RestResponse(dtoResultList);
            }
        }
        
        return new RestResponse();
    }
    
    /**
     * <b>URL: /region/listChildren</b>
     * 列出指定范围和状态的第一层孩子区域列表（需填父亲区域ID）
     */
    @RequireAuthentication(false)
    @RequestMapping("listChildren")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listChildren(@Valid ListRegionCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listChildRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getParentId(), 
            RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        if(dtoResultList != null){
            int hashCode = dtoResultList.hashCode();
            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
                return new RestResponse(dtoResultList);
            }
        }
        return new RestResponse();
    }
    
    /**
     * <b>URL: /region/listDescendants</b>
     * 列出指定范围和状态的所有层孩子区域列表（可不填父亲区域ID）
     */
    @RequireAuthentication(false)
    @RequestMapping("listDescendants")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listDescendants(@Valid ListRegionCommand cmd) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Region> entityResultList = this.regionProvider.listDescendantRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getParentId(), 
            RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
    
    /**
     * <b>URL: /region/listRegionByKeyword</b>
     * 根据关键字查询区域列表（可不填父亲区域ID）
     */
    @RequireAuthentication(false)
    @RequestMapping("listRegionByKeyword")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listRegionByKeyword(@Valid ListRegionByKeywordCommand cmd) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() == null)
            cmd.setSortBy("");
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<Region> entityResultList = this.regionProvider.listRegionByKeyword(cmd.getParentId(), 
            RegionScope.fromCode(cmd.getScope()), 
            RegionAdminStatus.fromCode(cmd.getStatus()), orderBy, cmd.getKeyword(), namespaceId);
        
        List<RegionDTO> dtoResultList = entityResultList.stream() 
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
    
    /**
     * <b>URL: /region/listActiveRegion</b>
     * 查询热门区域列表（sope可选，不填默认查询城市）
     */
    @RequireAuthentication(false)
    @RequestMapping("listActiveRegion")
    @RestReturn(value=RegionDTO.class, collection=true)
    public RestResponse listActiveRegion(@Valid ListActiveRegionCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        
        List<Region> entityResultList = this.regionProvider.listActiveRegion(RegionScope.fromCode(cmd.getScope()));
        
        List<RegionDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, RegionDTO.class); })
                .collect(Collectors.toList());
        if(dtoResultList != null){
            int hashCode = dtoResultList.hashCode();
            if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
                return new RestResponse(dtoResultList);
            }
        }
        return new RestResponse();
    }
}
