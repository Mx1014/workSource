package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>processorType：操作人类型 参考{@link com.everhomes.rest.decoration.ProcessorType}</li>
 * <li>id</li>
 * </ul>
 */
public class GetDecorationDetailCommand {
    private Long id;
    private Byte processorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Byte processorType) {
        this.processorType = processorType;
    }
}
