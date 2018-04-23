package com.everhomes.point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by xq.tian on 2018/2/5.
 */
public class PointEventProcessorHolder {

    private List<BasePointEventProcessor> processorList;

    public PointEventProcessorHolder() {
        this.processorList = new ArrayList<>();
    }

    public void doProcess(Consumer<BasePointEventProcessor> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Consumer should be not null.");
        }
        if (this.processorList != null) {
            for (BasePointEventProcessor processor : processorList) {
                consumer.accept(processor);
            }
        }
    }

    public void addProcessor(BasePointEventProcessor processor) {
        if (this.processorList == null) {
            this.processorList = new ArrayList<>();
        }
        this.processorList.add(processor);
    }

    public boolean notExist(String event) {
        if (processorList != null) {
            for (BasePointEventProcessor processor : this.processorList) {
                String[] init = processor.init();
                if (Stream.of(init).anyMatch(s -> s.equals(event))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isContinue(PointEventProcessorHolder processorHolder1) {
        return this.equals(processorHolder1);
    }
}