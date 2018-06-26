package com.everhomes.rest.parking;

/**
  *<ul>
  *<li>id : 车位id</li>
  *</ul>
  */

public class DeleteParkingSpaceCommand {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
