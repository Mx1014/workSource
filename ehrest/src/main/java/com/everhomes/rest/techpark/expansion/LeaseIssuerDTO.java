package com.everhomes.rest.techpark.expansion;

import com.everhomes.rest.address.AddressDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LeaseIssuerDTO {

    private Long communityId;
    private String issuerContact;
    private String issuerName;
    private List<AddressDTO> addresses;
    private Long enterpriseId;
}
