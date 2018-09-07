package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>moduleType: moduleType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>formOriginId: formOriginId</li>
 *     <li>formVersion: formVersion</li>
 * </ul>
 */
public class GetSelectedRequisitionFormCommand {

    private Long formOriginId;
    private Long formVersion;



    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

