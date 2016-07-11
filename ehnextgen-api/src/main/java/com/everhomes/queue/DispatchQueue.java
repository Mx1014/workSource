// @formatter:off
package com.everhomes.queue;

public interface DispatchQueue {
    void execAsync(DispatchableCommand command);
    
    Object exec(DispatchableCommand command);
    Object exec(DispatchableCommand command, int timeoutMs);
}
