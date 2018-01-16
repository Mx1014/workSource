package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by rui.jia  2018/1/16 10 :43
 */

public class OfflineSampleQualityInspectionResponse {

    @ItemType(SampleQualityInspectionDTO.class)
    List<SampleQualityInspectionDTO> sampleQualityInspections;

    @ItemType(QualityInspectionSpecificationDTO.class)
    private List<QualityInspectionSpecificationDTO> specifications;

    @ItemType(QualityInspectionSpecificationDTO.class)
    List<QualityInspectionSpecificationDTO> specificationsDetail;

    public List<SampleQualityInspectionDTO> getSampleQualityInspections() {
        return sampleQualityInspections;
    }

    public void setSampleQualityInspections(List<SampleQualityInspectionDTO> sampleQualityInspections) {
        this.sampleQualityInspections = sampleQualityInspections;
    }

    public List<QualityInspectionSpecificationDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<QualityInspectionSpecificationDTO> specifications) {
        this.specifications = specifications;
    }

    public List<QualityInspectionSpecificationDTO> getSpecificationsDetail() {
        return specificationsDetail;
    }

    public void setSpecificationsDetail(List<QualityInspectionSpecificationDTO> specificationsDetail) {
        this.specificationsDetail = specificationsDetail;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
