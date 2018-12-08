package com.everhomes.aclink.faceplusplus;

import com.everhomes.aclink.AclinkGroup;
import com.everhomes.aclink.AesUserKey;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAuth;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;
import com.everhomes.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FacePlusPlusService {

    FaceplusLoginResponse faceplusLogin (FaceplusLoginCommand cmd);


}
