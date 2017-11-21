package com.everhomes.relocation;

/**
 * @author sw on 2017/11/21.
 */
public interface RelocationProvider {

    RelocationRequest findRelocationRequestById(Long id);

    void createRelocationRequest(RelocationRequest request);

    void updateRelocationRequest(RelocationRequest request);
}
