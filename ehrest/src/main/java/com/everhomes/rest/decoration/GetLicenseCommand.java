package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>processorType：操作人类型 参考{@link com.everhomes.rest.decoration.ProcessorType}</li>
 * <li>workerId：查看自己的证明时不传 负责人查看工人时传工人id</li>
 *  <li>requestId</li>
 *  <li>phone : 工人查看自己的证明时需要传</li>
 * </ul>
 */
public class GetLicenseCommand {
    private Long requestId;
    private Long workerId;
    private Byte processorType;
    private String phone;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
