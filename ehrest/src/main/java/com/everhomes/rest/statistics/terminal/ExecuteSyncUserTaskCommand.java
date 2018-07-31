package com.everhomes.rest.statistics.terminal;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>genData: genData</li>
 *     <li>start: start</li>
 *     <li>end: end</li>
 * </ul>
 */
public class ExecuteSyncUserTaskCommand {

    private Integer namespaceId;

    private Integer genData;
    private String start;
    private String end;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getGenData() {
        return genData;
    }

    public void setGenData(Integer genData) {
        this.genData = genData;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
