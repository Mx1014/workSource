// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

@Component
public class AddressServiceImpl implements AddressService {

    @Override
    public List<CommunityDTO> listNearbyCommunities(ListNearbyCommunityCommand cmd) {
        List<CommunityDTO> results = new ArrayList<>();

        if(cmd.getCityId() == null && cmd.getLatigtue() == null && cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, need at least one valid value in one of these parameters: latitude, longitude, cityId");
        
        if(cmd.getLatigtue() != null && cmd.getLongitude() == null || 
                cmd.getLatigtue() == null && cmd.getLongitude() != null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");

        // ???
        
        return results;
    }
}
