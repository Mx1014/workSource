// @formatter:off
package com.everhomes.border;

import java.util.List;

/**
 * Border server administration interface
 * 
 * @author Kelven Yang
 *
 */
public interface BorderProvider {
    void createBorder(Border border);
    void updateBorder(Border border);
    void deleteBorderById(int id);
    Border findBorderById(int id);
    
    List<Border> listAllBorders();
}
