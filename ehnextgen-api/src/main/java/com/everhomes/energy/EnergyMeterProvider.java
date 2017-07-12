package com.everhomes.energy;

import java.util.List;

/**
 * 表记provider
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterProvider {

    /**
     * 创建表记
     * @return  返回id
     */
    long createEnergyMeter(EnergyMeter meter);

    /**
     * 根据id查询
     */
    EnergyMeter findById(Integer namespaceId, Long meterId);

    /**
     * 更新表记
     */
    void updateEnergyMeter(EnergyMeter meter);

    /**
     * 根据id集合获取表记
     */
    List<EnergyMeter> listByIds(Integer namespaceId, List<Long> ids);

    /**
     * 分页从数据库里列出表记
     * @param pageAnchor    锚点
     * @param pageSize  每页大小
     */
    List<EnergyMeter> listEnergyMeters(Long pageAnchor, Integer pageSize);

    /**
     * 查出所有的表
     * */
	List<EnergyMeter> listEnergyMeters();

    /**
     * 根据分类id查询表记
     */
    EnergyMeter findAnyByCategoryId(Integer namespaceId, Long categoryId);
}
