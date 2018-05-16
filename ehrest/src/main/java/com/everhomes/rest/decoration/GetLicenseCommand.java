package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>processorType：操作人类型 参考{@link com.everhomes.rest.decoration.ProcessorType}</li>
 * <li>uid：查看自己的证明时不传 负责人查看工人时传工人uid</li>
 * </ul>
 */
public class GetLicenseCommand {
    private Long requestId;
    private Long uid;
    private Byte processorType;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Byte processorType) {
        this.processorType = processorType;
    }
}
