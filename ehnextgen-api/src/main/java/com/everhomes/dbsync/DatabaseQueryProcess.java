package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.Table;
import org.jooq.impl.DSL;

public class DatabaseQueryProcess {
    NashornObjectService service;
    DatabaseQuery query;
    SelectJoinStep<Record> firstStep;
    SelectJoinStep<Record> joinStep;
    SelectOnConditionStep<Record> onCondStep;
    Pattern pParam = Pattern.compile("\\$\\w+");
    
    public DatabaseQueryProcess(NashornObjectService service, DatabaseQuery query) {
        this.service = service;
        this.query = query;
    }
    
    private void processFields(Map<String, Integer> inMap, List<Field<?>> fields, DataGraph dataGraph) {
        if(inMap.containsKey("graph:" + dataGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + dataGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        DataTable table = service.getTableMeta(dataGraph.getTable().getTableName());
        fields.addAll(table.getFields(dataGraph.getTable().getFields()));
        inMap.put("graph:" + dataGraph.getGraphName(), 1);
        inMap.put("table:" + dataGraph.getTable().getTableName(), 1);
        
        for(GraphRefer ref : dataGraph.getRefer()) {
            DataGraph newGraph = ref.getGraph();
            if(!ref.getJoinType().equals(NJoinType.NO_JOIN.getCode())) {
                processFields(inMap, fields, newGraph);    
            }
        }
    }
    
    private void processJoins(Map<String, Integer> inMap, DataGraph dataGraph, GraphRefer ref) {
        if(inMap.containsKey("graph:" + dataGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + dataGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        inMap.put("graph:" + dataGraph.getGraphName(), 1);
        inMap.put("table:" + dataGraph.getTable().getTableName(), 1);
        
        DataTable table = service.getTableMeta(dataGraph.getTable().getTableName());
        DataGraph newGraph = ref.getGraph();
        DataTable newTable = service.getTableMeta(newGraph.getTable().getTableName());
        
        SelectOnStep<Record> onStep = null;
        NJoinType nType = NJoinType.fromCode(ref.getJoinType());
        if(nType.equals(NJoinType.INNER_JOIN)) {
            onStep = joinStep.join(newTable.getTableJOOQ());    
        } else if(nType.equals(NJoinType.LEFT_OUTER_JOIN)) {
            onStep = joinStep.leftOuterJoin(newTable.getTableJOOQ()); 
        } else if(nType.equals(NJoinType.RIGHT_OUTER_JOIN)) {
            onStep = joinStep.rightOuterJoin(newTable.getTableJOOQ());
        }
        
        if(onStep != null) {
            Field pf = table.getField(ref.getParentField());
            Field cf = newTable.getField(ref.getChildField());
            onCondStep = onStep.on(pf.eq(cf));
            joinStep = onCondStep;
        }
        
        for(GraphRefer newRef : newGraph.getRefer()) {
            processJoins(inMap, newGraph, newRef);
        }
    }
    
    private Condition parseCondition(String text, Map<String, String> inputs) {
        Matcher m = pParam.matcher(text);
        System.out.println(m.find());
        System.out.println(m.matches());
        
        return null;
    }
    
    public Result<Record> processQuery() {
        DataGraph graph = service.getGraph(query.getGraphName());
        GraphTable graphTable = graph.getTable();
        DataTable table = service.getTableMeta(graphTable.getTableName());
        List<Field<?>> fields = new ArrayList<Field<?>>();
        Map<String, Integer> inMap = new HashMap<String, Integer>();
        processFields(inMap, fields, graph);
        
        firstStep = DSL.using(service.configure()).select(fields).from(table.getTableName());
        joinStep = firstStep;
        inMap = new HashMap<String, Integer>();
        for(GraphRefer newRef : graph.getRefer()) {
            processJoins(inMap, graph, newRef);
        }
        
        for(String cond : query.getConditions()) {
            //eh_users.id = $user_id and eh_door_user_permission.user_id = $user_id
//            DSL.condition(sql, bindings);
        }
        
        return null;
    }
}
