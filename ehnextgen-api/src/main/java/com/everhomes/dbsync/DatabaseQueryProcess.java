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
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;

public class DatabaseQueryProcess {
    NashornObjectService service;
    DatabaseQuery query;
    SelectJoinStep<Record> joinStep;
    SelectOnConditionStep<Record> onCondStep;
    Pattern pParam = Pattern.compile("\\$\\w+");
    
    public DatabaseQueryProcess(NashornObjectService service, DatabaseQuery query) {
        this.service = service;
        this.query = query;
    }
    
    private void processFields(Map<String, Integer> inMap, List<Field<?>> fields, DataGraph dataGraph) throws Exception {
        if(inMap.containsKey("graph:" + dataGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + dataGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        DataTable table = service.getTableMeta(dataGraph.getTable().getTableName());
        checkError(table, "table not found");
        
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
    
    private Condition parseCondition(String sql, Map<String, String> inputs) throws Exception {
        //String sql = "eh_users.id = $id and eh_door_user_permission.user_id = $user_id;";
        String newSql = "";
        int start = 0;
        Matcher m = pParam.matcher(sql);
        List<Object> objs = new ArrayList<Object>();
        while(m.find()) {
            newSql += sql.substring(start, m.start());
            newSql += "?";
            start = m.end();
            String sub = sql.substring(m.start(), m.end());
            sub = sub.substring(1);
            if(!inputs.containsKey(sub)) {
                throw new Exception("sql binding error");
            }
            objs.add(inputs.get(sub));
            
        }
        
        if(start > 0) {
            newSql += sql.substring(start, sql.length());
        }
        
        if(objs.size() == 0) {
            return DSL.condition(sql);
        }
        
        return DSL.condition(newSql, objs.toArray());
    }
    
    private void checkError(Object o, String msg) throws Exception {
        if(o == null) {
            throw new Exception(msg);
        }
    }
    
    public Result<Record> processQuery() throws Exception {
        DataGraph graph = service.getGraph(query.getGraphName());
        checkError(graph, "graph not foud");
        
        GraphTable graphTable = graph.getTable();
        DataTable table = service.getTableMeta(graphTable.getTableName());
        List<Field<?>> fields = new ArrayList<Field<?>>();
        Map<String, Integer> inMap = new HashMap<String, Integer>();
        processFields(inMap, fields, graph);
        
        joinStep = DSL.using(service.configure()).select(fields).from(table.getTableName());
        inMap = new HashMap<String, Integer>();
        for(GraphRefer newRef : graph.getRefer()) {
            processJoins(inMap, graph, newRef);
        }
        
        List<Condition> conds = new ArrayList<Condition>();
        for(String cond : query.getConditions()) {
            conds.add(parseCondition(cond, query.getInputs()));
        }
        
        List<SortField> sorts = new ArrayList<SortField>();
        for(String sort : query.getOrders()) {
            String[] subs = sort.split(" ");
            String asc = "asc"; 
            if(subs.length == 2) {
                asc = subs[1];
            }
            
            if(asc.equals("asc")) {
                Field<?> f = service.getTableField(subs[0]);
                sorts.add(f.asc());
            } else {
                Field<?> f = service.getTableField(subs[0]);
                sorts.add(f.desc());
            }
        }
        
        Result<Record> records = null;
        if(conds.size() > 0) {
            if(sorts.size() > 0) {
                records = joinStep.where(conds).orderBy(sorts.toArray(new SortField[sorts.size()])).fetch();
            } else {
                records = joinStep.where(conds).fetch();
            }
            
        } else {
            if(sorts.size() > 0) {
                records = joinStep.orderBy(sorts.toArray(new SortField[sorts.size()])).fetch();
            } else {
                records = joinStep.fetch();
            }
            
        }
        
        return records;
    }
    
    public void setService(NashornObjectService service) {
        this.service = service;
    }
}
