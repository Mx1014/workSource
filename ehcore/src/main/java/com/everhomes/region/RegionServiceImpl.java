package com.everhomes.region;

import com.everhomes.rest.region.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sfyan on 2016/10/11.
 */
@Component
public class RegionServiceImpl implements RegionService{

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    private RegionProvider regionProvider;

    @Override
    public List<RegionCodeDTO> listRegionCodes() {
        List<RegionCodes> regionCodes = regionProvider.listRegionCodes(null, null);
        if(null == regionCodes){
            return new ArrayList<RegionCodeDTO>();
        }
        List<RegionCodeDTO> regionCodeDTOs = regionCodes.stream().map(r -> {
            RegionCodeDTO dto= ConvertHelper.convert(r, RegionCodeDTO.class);
            dto.setRegionCode("+" + r.getCode());
            return dto;
        }).collect(Collectors.toList());
        return regionCodeDTOs;
    }

    @Override
    public void createRegionCode(RegionCodeDTO dto) {

        List<RegionCodes> regionCodes = regionProvider.listRegionCodes(dto.getName(), null);

        if(null != regionCodes && regionCodes.size() > 0){
            LOGGER.error("create region code error. cmd = {}", dto);
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGIONCODE_NAME_EXISTING,
                    "create region code error");
        }

        regionCodes = regionProvider.listRegionCodes(null, dto.getCode());

        if(null != regionCodes && regionCodes.size() > 0){
            LOGGER.error("create region code error. cmd = {}", dto);
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGIONCODE_CODE_EXISTING,
                    "create region code error");
        }

        RegionCodes regionCode = ConvertHelper.convert(dto, RegionCodes.class);
        regionCode.setPinyin(PinYinHelper.getPinYin(regionCode.getName()));
        regionCode.setFirstLetter(PinYinHelper.getCapitalInitial(regionCode.getPinyin()));
        regionCode.setStatus(RegionCodeStatus.ACTIVE.getCode());
        regionProvider.createRegionCode(regionCode);
    }

    @Override
    public void updateRegionCode(RegionCodeDTO dto) {
        List<RegionCodes> regionCodes = regionProvider.listRegionCodes(dto.getName(), null);

        RegionCodes regionCode = regionProvider.findRegionCodeById(dto.getId());

        if(!regionCode.getName().equals(dto.getName())){
            regionCodes = regionProvider.listRegionCodes(dto.getName(), null);
            if(null != regionCodes && regionCodes.size() > 0){
                LOGGER.error("create region code error. cmd = {}", dto);
                throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGIONCODE_NAME_EXISTING,
                        "create region code error");
            }
        }

        if(!regionCode.getCode().equals(dto.getCode())){
            regionCodes = regionProvider.listRegionCodes(null, dto.getCode());
            if(null != regionCodes && regionCodes.size() > 0){
                LOGGER.error("create region code error. cmd = {}", dto);
                throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGIONCODE_CODE_EXISTING,
                        "create region code error");
            }
        }

        regionCode = ConvertHelper.convert(dto, RegionCodes.class);
        regionCode.setPinyin(PinYinHelper.getPinYin(regionCode.getName()));
        regionCode.setFirstLetter(PinYinHelper.getCapitalInitial(regionCode.getPinyin()));
        regionCode.setStatus(RegionCodeStatus.ACTIVE.getCode());
        regionProvider.updateRegionCode(regionCode);
    }

    @Override
    public RegionDTO findRegionById(GetRegionCommand cmd) {
        RegionDTO regionDTO = ConvertHelper.convert(this.regionProvider.findRegionById(cmd.getId()), RegionDTO.class);
        return regionDTO;
    }

    @Override
    public RegionTreeResponse regionTree(RegionTreeCommand cmd) {
        RegionTreeResponse response = new RegionTreeResponse();
        if(null == cmd.getNamespaceId())
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        if(cmd.getParentId() == null){
            cmd.setParentId(0L);
        }

        List<RegionTreeDTO> listDto = getRegionTree(cmd);

        response.setList(listDto);
        return response;

    }

    private List<RegionTreeDTO> getRegionTree(RegionTreeCommand cmd) {
        List<RegionTreeDTO> listDto = new ArrayList<RegionTreeDTO>();

        List<Region> regions = this.regionProvider.listChildRegions( cmd.getNamespaceId(), cmd.getParentId(),null, RegionAdminStatus.ACTIVE);

        if(regions == null || regions.size() == 0){
            return null;
        }

        for(int i = 0; i<regions.size(); i++){

            RegionTreeDTO chilDto = ConvertHelper.convert(regions.get(i), RegionTreeDTO.class);
            RegionTreeCommand chiCmd = new RegionTreeCommand();
            chiCmd.setParentId(regions.get(i).getId());
            chiCmd.setNamespaceId(cmd.getNamespaceId());
            List<RegionTreeDTO> chilChils = getRegionTree(chiCmd);
            if(chilChils != null && chilChils.size() > 0){
                chilDto.setChildren(chilChils);
            }
            listDto.add(chilDto);
        }

        return listDto;
    }
}
