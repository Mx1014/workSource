package com.everhomes.border;

/**
 * 
 * WebSocket based Core-Border connection management
 * 
 * @author Kelven Yang
 *
 */
public interface BorderConnectionProvider {
    BorderConnection getBorderConnection(int borderId);
}
