package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.util.ConvertHelper;
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

	public static DataGraph fromJSDataGraph(String graphJson) {
		JSDataGraphObject obj = ConvertHelper.convert(graphJson, JSDataGraphObject.class);
		String graphName = obj.getAppName() + "$" + obj.getMapName();
		
		Map<String, DataGraph> tree = new HashMap<String, DataGraph>();
		
		for(String t : obj.getTables()) {
			DataGraph dataGraph = new DataGraph();
			GraphTable table = new GraphTable();
			dataGraph.setGraphName(t);
			table.setTableName(t);
			dataGraph.setTable(table);
			
			JSMappingObjectItem mItem = obj.getMapping().get(t);
			for(String f : mItem.getFields()) {
				table.addField(f);
			}
			
			tree.put(dataGraph.getGraphName(), dataGraph);
		}
		
		DataGraph topGraph = tree.get(obj.getTables().get(0));
		
		//link all graph to topGraph
		for(String t : obj.getTables()) {
			DataGraph dataGraph = tree.get(t);
			JSMappingObjectItem mItem = obj.getMapping().get(t);
			if(mItem.getBelong() != null) {
				
				for(JSMappingBelongObject item : mItem.getBelong()) {
					GraphRefer ref = new GraphRefer();
					NJoinType join = NJoinType.fromCode(item.getJoin());
					ref.setJoinType(join.getCode());
					ref.setChildField(item.getFieldB());
					ref.setParentField(item.getFieldA());
					
					DataGraph subGraph = tree.get(item.getTable());
					ref.setGraph(subGraph);
					dataGraph.addRefer(ref);
				}				
				
				for(JSMappingHasObject item: mItem.getHasMany()) {
					GraphRefer ref = new GraphRefer();
					NJoinType join = NJoinType.fromCode(item.getJoin());
					ref.setJoinType(join.getCode());
					ref.setChildField(item.getFieldB());
					ref.setParentField(mItem.getPrimary());
					
					DataGraph subGraph = tree.get(item.getTable());
					ref.setGraph(subGraph);
					dataGraph.addRefer(ref);
					dataGraph.addRefer(ref);
				}
				
				for(JSMappingHasObject item: mItem.getHasOne()) {
					GraphRefer ref = new GraphRefer();
					NJoinType join = NJoinType.fromCode(item.getJoin());
					ref.setJoinType(join.getCode());
					ref.setChildField(item.getFieldB());
					ref.setParentField(mItem.getPrimary());
					
					DataGraph subGraph = tree.get(item.getTable());
					ref.setGraph(subGraph);
					dataGraph.addRefer(ref);
					dataGraph.addRefer(ref);
				}
				
			}			
		}
		
		topGraph.setGraphName(graphName);
		return topGraph;
	}
}
