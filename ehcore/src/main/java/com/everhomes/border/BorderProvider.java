package com.everhomes.border;

import java.util.List;

public interface BorderProvider {
    void createBorder(Border border);
    void updateBorder(Border border);
    void deleteBorderById(int id);
    Border findBorderById(int id);
    
    List<Border> listAllBorders();
}
