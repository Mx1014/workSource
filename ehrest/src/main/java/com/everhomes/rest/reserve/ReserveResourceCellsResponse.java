package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>timeType: 时间类型 {@link com.everhomes.rest.reserve.ReserveTimeType}</li>
 * <li>timeUnit: 时间单元</li>
 * <li>multiFlag: 是否支持多选 {@link com.everhomes.rest.reserve.ReserveCommonFlag}</li>
 * <li>cells: 按纵坐标计算的单元格列表 {@link com.everhomes.rest.reserve.ReserveCellOrdinateDTO}</li>
 * <li>abscissaTimes: 横坐标时间</li>
 * </ul>
 */
public class ReserveResourceCellsResponse {

    private Byte timeType;
    private Integer timeUnit;
    private Byte multiFlag;

    @ItemType(ReserveCellOrdinateDTO.class)
    private List<ReserveCellOrdinateDTO> cellOrdinateDTOS;

    @ItemType(Long.class)
    private List<Long> abscissaTimes;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getTimeType() {
        return timeType;
    }

    public void setTimeType(Byte timeType) {
        this.timeType = timeType;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Byte getMultiFlag() {
        return multiFlag;
    }

    public void setMultiFlag(Byte multiFlag) {
        this.multiFlag = multiFlag;
    }

    public List<ReserveCellOrdinateDTO> getCellOrdinateDTOS() {
        return cellOrdinateDTOS;
    }

    public void setCellOrdinateDTOS(List<ReserveCellOrdinateDTO> cellOrdinateDTOS) {
        this.cellOrdinateDTOS = cellOrdinateDTOS;
    }

    public List<Long> getAbscissaTimes() {
        return abscissaTimes;
    }

    public void setAbscissaTimes(List<Long> abscissaTimes) {
        this.abscissaTimes = abscissaTimes;
    }
}
