package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>workerId：工人id</li>
 *  <li>processorType：操作人类型 参考{@link com.everhomes.rest.decoration.ProcessorType}</li>
 * </ul>
 */
public class GetQrDetailCommand {

    private Long workerId;
    private Byte processorType;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Byte getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Byte processorType) {
        this.processorType = processorType;
    }
}
