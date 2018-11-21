package com.everhomes.aclink;


import com.everhomes.rest.aclink.DoorAccessDTO;

import java.util.Comparator;
import java.util.List;

public class DoorAccessComparator implements Comparator<DoorAccessDTO> {
    @Override
    public int compare(DoorAccessDTO door1, DoorAccessDTO door2) {
        // if door1 is null then should put door2 ahead of door1
        if(door1 == null || door1.getId() == null) {
            return 1;
        } else {
            if(door2 == null | door2.getId() == null) {
                return -1;
            } else {
                return door1.getDisplayName().compareTo(door2.getDisplayName());
//                char[] name1 = door1.getDisplayName().toCharArray();
//                char[] name2 = door2.getDisplayName().toCharArray();
//                for(int i = 0; i < name2.length; i++){
//                    if(name1[i] == '\0' || name1[i] < name2[i]){
//                        return -1;
//                    }
//                    if(name1[i] == name2[i]){
//                        continue;
//                    }
//                    if(name1[i] > name2[i]){
//                        return 1;
//                    }
//                }
//                return 0;
            }
        }
    }
}
