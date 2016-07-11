// @formatter:off
package com.everhomes.queue;

public interface DispatchQueueProvider {
    DispatchQueue getQueue(String name);
}
