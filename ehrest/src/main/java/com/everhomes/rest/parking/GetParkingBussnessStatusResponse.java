// @formatter:off
package com.everhomes.rest.parking;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>V6.6
 * <li>dockingFuncLists: 对接功能列表，{@link com.everhomes.rest.parking.ParkingFuncDTO}</li>
 * <li>funcLists: 非对接功能列表，{@link com.everhomes.rest.parking.ParkingFuncDTO}</li>
 * <li>enableMonthCard:  月卡申请功能是否启用, 0：不启用，1：启用 {@link com.everhomes.rest.parking.ParkingConfigFlag}</li>
 * <li>monthCardFlow:  当前月卡工作流标识，参考 {@link com.everhomes.rest.parking.ParkingRequestFlowType}</li>
 * <li>flowModeList: 月卡模式列表， 参考{@link com.everhomes.rest.parking.ParkingRequestFlowType}</li>
 * </ul>
 */
public class GetParkingBussnessStatusResponse {
    private List<ParkingFuncDTO> dockingFuncLists;

    private List<ParkingFuncDTO> funcLists;

    private Byte enableMonthCard;

    private Byte monthCardFlow;

    private List<Byte> flowModeList;

    private String defaultData;
    
    private String defaultPlate;

    
    public List<ParkingFuncDTO> getDockingFuncLists() {
        return dockingFuncLists;
    }

    public void setDockingFuncLists(List<ParkingFuncDTO> dockingFuncLists) {
        this.dockingFuncLists = dockingFuncLists;
    }

    public List<ParkingFuncDTO> getFuncLists() {
        return funcLists;
    }

    public void setFuncLists(List<ParkingFuncDTO> funcLists) {
        this.funcLists = funcLists;
    }

    public Byte getEnableMonthCard() {
        return enableMonthCard;
    }

    public void setEnableMonthCard(Byte enableMonthCard) {
        this.enableMonthCard = enableMonthCard;
    }

    public Byte getMonthCardFlow() {
        return monthCardFlow;
    }

    public void setMonthCardFlow(Byte monthCardFlow) {
        this.monthCardFlow = monthCardFlow;
    }

    public List<Byte> getFlowModeList() {
        return flowModeList;
    }

    public void setFlowModeList(List<Byte> flowModeList) {
        this.flowModeList = flowModeList;
    }

    public String getDefaultData() {
		return defaultData;
	}

	public void setDefaultData(String defaultData) {
		this.defaultData = defaultData;
	}

	public String getDefaultPlate() {
		return defaultPlate;
	}

	public void setDefaultPlate(String defaultPlate) {
		this.defaultPlate = defaultPlate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
