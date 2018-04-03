package com.everhomes.point;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xq.tian on 2017/12/7.
 */
public class PointEventGroup {

    private final transient ReentrantLock lock = new ReentrantLock();

    private PointRuleCategory category;

    public PointEventGroup(PointRuleCategory category) {
        this.category = category;
    }

    public PointRuleCategory getCategory() {
        return category;
    }

    public boolean tryLock() {
        return lock.tryLock();
    }

    public void unlock() {
        lock.unlock();
    }
}
