package com.everhomes.rest.decoration;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor</li>
 * <li>workers:List{@link com.everhomes.rest.decoration.DecorationWorkerDTO}</li>
 * </ul>
 */
public class ListWorkersResponse {

    private Long nextPageAnchor;
    private List<DecorationWorkerDTO> workers;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DecorationWorkerDTO> getWorkers() {
        return workers;
    }

    public void setWorkers(List<DecorationWorkerDTO> workers) {
        this.workers = workers;
    }
}
