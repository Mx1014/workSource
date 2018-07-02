// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>invalidTimeFlag : (必填)有效期是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>invalidTimeVisibleFlag : (必填)有效期是否可见，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>plateNoFlag : (必填)车牌号码是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>plateNoVisibleFlag : (必填)车牌号码是否可见，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>idNumberFlag : (必填)证件号码是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>idNumberVisibleFlag : (必填)证件号码是否可见，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>visitFloorFlag : (必填)到访楼层是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>visitFloorVisibleFlag : (必填)到访楼层是否可见，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>visitAddressesFlag : (必填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *<li>visitAddressesVisibleFlag : (必填)到访门牌是否可见，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *</ul>
  */
public class VisitorsysFormConfig {
    private Byte invalidTimeFlag = 0;
    private Byte invalidTimeVisibleFlag = 0;
    private Byte plateNoFlag = 0;
    private Byte plateNoVisibleFlag = 0;
    private Byte idNumberFlag = 0;
    private Byte idNumberVisibleFlag = 0;
    private Byte visitFloorFlag = 0;
    private Byte visitFloorVisibleFlag = 0;
    private Byte visitAddressesFlag = 0;
    private Byte visitAddressesVisibleFlag = 0;

    public Byte getInvalidTimeFlag() {
        return invalidTimeFlag;
    }

    public void setInvalidTimeFlag(Byte invalidTimeFlag) {
        this.invalidTimeFlag = invalidTimeFlag;
    }

    public Byte getInvalidTimeVisibleFlag() {
        return invalidTimeVisibleFlag;
    }

    public void setInvalidTimeVisibleFlag(Byte invalidTimeVisibleFlag) {
        this.invalidTimeVisibleFlag = invalidTimeVisibleFlag;
    }

    public Byte getPlateNoFlag() {
        return plateNoFlag;
    }

    public void setPlateNoFlag(Byte plateNoFlag) {
        this.plateNoFlag = plateNoFlag;
    }

    public Byte getPlateNoVisibleFlag() {
        return plateNoVisibleFlag;
    }

    public void setPlateNoVisibleFlag(Byte plateNoVisibleFlag) {
        this.plateNoVisibleFlag = plateNoVisibleFlag;
    }

    public Byte getIdNumberFlag() {
        return idNumberFlag;
    }

    public void setIdNumberFlag(Byte idNumberFlag) {
        this.idNumberFlag = idNumberFlag;
    }

    public Byte getIdNumberVisibleFlag() {
        return idNumberVisibleFlag;
    }

    public void setIdNumberVisibleFlag(Byte idNumberVisibleFlag) {
        this.idNumberVisibleFlag = idNumberVisibleFlag;
    }

    public Byte getVisitFloorFlag() {
        return visitFloorFlag;
    }

    public void setVisitFloorFlag(Byte visitFloorFlag) {
        this.visitFloorFlag = visitFloorFlag;
    }

    public Byte getVisitFloorVisibleFlag() {
        return visitFloorVisibleFlag;
    }

    public void setVisitFloorVisibleFlag(Byte visitFloorVisibleFlag) {
        this.visitFloorVisibleFlag = visitFloorVisibleFlag;
    }

    public Byte getVisitAddressesFlag() {
        return visitAddressesFlag;
    }

    public void setVisitAddressesFlag(Byte visitAddressesFlag) {
        this.visitAddressesFlag = visitAddressesFlag;
    }

    public Byte getVisitAddressesVisibleFlag() {
        return visitAddressesVisibleFlag;
    }

    public void setVisitAddressesVisibleFlag(Byte visitAddressesVisibleFlag) {
        this.visitAddressesVisibleFlag = visitAddressesVisibleFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
