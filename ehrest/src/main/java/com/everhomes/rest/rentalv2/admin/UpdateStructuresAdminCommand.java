package com.everhomes.rest.rentalv2.admin;

import com.everhomes.rest.rentalv2.SiteStructureDTO;

import java.util.List;

public class UpdateStructuresAdminCommand {
    private List<SiteStructureDTO> structures;

    public List<SiteStructureDTO> getStructures() {
        return structures;
    }

    public void setStructures(List<SiteStructureDTO> structures) {
        this.structures = structures;
    }
}
