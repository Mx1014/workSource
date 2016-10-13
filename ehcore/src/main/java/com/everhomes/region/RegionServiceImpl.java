package com.everhomes.region;

import com.everhomes.rest.region.RegionCodeDTO;
import com.everhomes.rest.region.RegionCodeStatus;
import com.everhomes.rest.region.RegionServiceErrorCode;
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
}
