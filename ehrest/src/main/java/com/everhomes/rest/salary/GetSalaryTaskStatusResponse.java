package com.everhomes.rest.salary;

/**
 *
 * <ul>返回值:
 * <li>status:  1-OK, 0-不OK</li>
 * </ul>
 */
public class GetSalaryTaskStatusResponse {
    private Byte status;

    public GetSalaryTaskStatusResponse() {

    }

    public GetSalaryTaskStatusResponse(Byte status) {
        super();
        this.status = status;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
