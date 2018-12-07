package com.everhomes.aclink;


import com.everhomes.rest.aclink.DoorAccessDTO;

import java.util.Comparator;
import java.util.List;

public class DoorAccessComparator implements Comparator<DoorAccessDTO> {
    @Override
    public int compare(DoorAccessDTO door1, DoorAccessDTO door2) {
        if(door1 == null || door1.getId() == null) {
            return 1;
        } else {
            if(door2 == null | door2.getId() == null) {
                return -1;
            } else {
                return door1.getDisplayName().compareTo(door2.getDisplayName());
            }
        }
    }
}
