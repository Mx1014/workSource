package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.StringHelper;

public class DataGraph {
    //use name for cache
    private String graphName;
    private GraphTable table;
    private List<GraphRefer> refer; //TODO belongTo ? hashMany through table ?
    
    public DataGraph() {
        this.refer = new ArrayList<GraphRefer>();
    }
    
    public String getGraphName() {
        if(graphName == null || graphName.isEmpty()) {
            return this.table.getTableName();
        }
        return graphName;
    }
    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }
    public GraphTable getTable() {
        return table;
    }
    public void setTable(GraphTable table) {
        this.table = table;
    }

    public List<GraphRefer> getRefer() {
        return refer;
    }

    public void setRefer(List<GraphRefer> refer) {
        this.refer = refer;
    }
    
    public void addRefer(GraphRefer refer) {
        this.refer.add(refer);
    }
}
