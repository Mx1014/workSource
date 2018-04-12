package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>enterprises {@link com.everhomes.rest.enterprise.EnterpriseDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class ListEnterpriseResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseDTO.class)
    private List<EnterpriseDTO> enterprises;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnterpriseDTO> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<EnterpriseDTO> enterprises) {
        this.enterprises = enterprises;
    }
    
    
}
