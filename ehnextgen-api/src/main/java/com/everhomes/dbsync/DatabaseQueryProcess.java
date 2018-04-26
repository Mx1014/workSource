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
import org.jooq.Row;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseQueryProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryProcess.class);
    
    NashornObjectService service;
    DatabaseQuery query;
    SelectJoinStep<Record> joinStep;
    SelectOnConditionStep<Record> onCondStep;
    private static final Pattern pParam = Pattern.compile("\\$\\w+");
    private List<Map<String, Object>> results = null;
    private List<Field<?>> fields;
    private List<String> fieldNames;
    private GraphRefer parentRef = null;
    
    public DatabaseQueryProcess(NashornObjectService service, DatabaseQuery query) {
        this(service, query, null, null);
    }
    
    public DatabaseQueryProcess(NashornObjectService service, DatabaseQuery query, List<Map<String, Object>> results, GraphRefer pref) {
        this.service = service;
        this.query = query;
        if(results != null) {
            this.results = results;
            this.parentRef = pref;
        } else {
            this.results = new ArrayList<Map<String, Object>>();
        }
        
        this.fields = new ArrayList<Field<?>>();
        this.fieldNames = new ArrayList<String>();
    }
    
    private void processFields(Map<String, Integer> inMap, DataGraph dataGraph, GraphRefer ref) throws Exception {
        DataGraph newGraph = ref.getGraph();
        DataTable newTable = service.getTableMeta(newGraph.getTable().getTableName());
        checkError(newTable, "newTable not found");
        
        if(inMap.containsKey("graph:" + newGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + newGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        inMap.put("graph:" + newGraph.getGraphName(), 1);
        inMap.put("table:" + newGraph.getTable().getTableName(), 1);
        
        //do real work
        NJoinType nType = NJoinType.fromCode(ref.getJoinType());
        if(!nType.equals(NJoinType.NO_JOIN)) {
            String tableName = newGraph.getTable().getTableName();
            List<Field<?>> newFields = newTable.getFields(newGraph.getTable().getFields());
            for(Field<?> field : newFields) {
                fieldNames.add(tableName + "$" + field.getName());
            }
            this.fields.addAll(newFields);
        }
        
        for(GraphRefer newRef : newGraph.getRefer()) {
            processFields(inMap, newGraph, newRef);
        }
        
    }
    
    private void processJoins(Map<String, Integer> inMap, DataGraph dataGraph, GraphRefer ref) {
        DataTable table = service.getTableMeta(dataGraph.getTable().getTableName());
        DataGraph newGraph = ref.getGraph();
        DataTable newTable = service.getTableMeta(newGraph.getTable().getTableName());
        
        if(inMap.containsKey("graph:" + newGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + newGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        inMap.put("graph:" + newGraph.getGraphName(), 1);
        inMap.put("table:" + newGraph.getTable().getTableName(), 1);
        
        //now do real work
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
    
    private void processRecords(Map<String, Integer> inMap, Map<String, Object> result, DataGraph dataGraph, GraphRefer ref) throws Exception {
        DataGraph newGraph = ref.getGraph();
        DataTable newTable = service.getTableMeta(newGraph.getTable().getTableName());
        checkError(newTable, "newTable not found");
        
        if(inMap.containsKey("graph:" + newGraph.getGraphName())) {
            // dead reference reach
            return;
        }
        
        if(inMap.containsKey("table:" + newGraph.getTable().getTableName())) {
            // already process
            return;
        }
        
        inMap.put("graph:" + newGraph.getGraphName(), 1);
        inMap.put("table:" + newGraph.getTable().getTableName(), 1);
        
        //do real work
        NJoinType nType = NJoinType.fromCode(ref.getJoinType());
        if(nType.equals(NJoinType.NO_JOIN)) {
            //Do a sub query here
            DatabaseQuery subQuery = new DatabaseQuery();
            subQuery.setPageSize(query.getPageSize());
            subQuery.setDataGraph(newGraph);
            //TODO support more conditions, and merge the parent conditions
            subQuery.addCondition(newGraph.getTable().getTableName(), ref.getChildField() + " = $" + ref.getChildField());
            
            Map<String, Object> pResult = (Map<String, Object>) result.get(dataGraph.getTable().getTableName());
            checkError(pResult, "parent result not found");
            subQuery.putInput(ref.getChildField(), pResult.get(ref.getParentField()).toString());
            List<Map<String, Object>> subResults = new ArrayList<Map<String, Object>>();
            pResult.put(newGraph.getTable().getTableName(), subResults);
            
            DatabaseQueryProcess subProcess = new DatabaseQueryProcess(service, subQuery, subResults, ref);
            subProcess.processQuery();
        }
        
        for(GraphRefer newRef : newGraph.getRefer()) {
            processRecords(inMap, result, newGraph, newRef);
        }
    }
    
    private void processRecord(DataGraph graph, DataTable table, Result<Record> records) throws Exception {
        int size = records.size();
        
        for(int i = 0; i < size; i++) {
            Record r = records.get(i);
            Map<String, Object> result = new HashMap<String, Object>();
            
            //Step1: get all objects
            for(int j = 0; j < this.fieldNames.size(); j++) {
                String fieldName = this.fieldNames.get(j);
                String[] ss = fieldName.split("\\$");
                Object val = r.getValue(j);
                
                Map<String, Object> obj = (Map<String, Object>)result.getOrDefault(ss[0], new HashMap<String, Object>());
                if(obj.size() == 0) {
                    result.put(ss[0], obj);
                }
                obj.put(ss[1], val);
            }
            
            //Step2: make sub queries
            Map<String, Integer> inMap = new HashMap<String, Integer>();
            inMap.put("graph:" + graph.getGraphName(), 1);
            inMap.put("table:" + graph.getTable().getTableName(), 1);
            for(GraphRefer newRef : graph.getRefer()) {
                processRecords(inMap, result, graph, newRef);    
            }
            
            this.results.add(result);
        }
    }
    
    private void checkError(Object o, String msg) throws Exception {
        if(o == null) {
            throw new Exception(msg);
        }
    }
    
    public List<Map<String, Object>> processQuery() throws Exception {
        DataGraph graph = this.query.getDataGraph();
        checkError(graph, "graph not foud");
        
        GraphTable graphTable = graph.getTable();
        DataTable table = service.getTableMeta(graphTable.getTableName());
        
        Map<String, Integer> inMap = new HashMap<String, Integer>();
        inMap.put("graph:" + graph.getGraphName(), 1);
        inMap.put("table:" + graph.getTable().getTableName(), 1);
        //do real work
        String tableName = graph.getTable().getTableName();
        List<Field<?>> newFields = table.getFields(graph.getTable().getFields());
        for(Field<?> field : newFields) {
            fieldNames.add(tableName + "$" + field.getName());
        }
        this.fields.addAll(newFields);
        
        for(GraphRefer newRef : graph.getRefer()) {
            processFields(inMap, graph, newRef);    
        }
        
        inMap = new HashMap<String, Integer>();
        inMap.put("graph:" + graph.getGraphName(), 1);
        inMap.put("table:" + graph.getTable().getTableName(), 1);
        //do real work
        joinStep = DSL.using(service.configure()).select(fields).from(table.getTableName());
        for(GraphRefer newRef : graph.getRefer()) {
            processJoins(inMap, graph, newRef);
        }
        
        List<Condition> conds = new ArrayList<Condition>();
        List<String> condStrs = query.getConditions(graph.getTable().getTableName());
        for(String cond : condStrs) {
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
                records = joinStep.where(conds).orderBy(sorts.toArray(new SortField[sorts.size()])).limit(query.getPageSize()).fetch();
            } else {
                records = joinStep.where(conds).limit(query.getPageSize()).fetch();
            }
            
        } else {
            if(sorts.size() > 0) {
                records = joinStep.orderBy(sorts.toArray(new SortField[sorts.size()])).limit(query.getPageSize()).fetch();
            } else {
                records = joinStep.limit(query.getPageSize()).fetch();
            }
            
        }
        
        processRecord(graph, table, records);
        
        return this.results;
    }
    
    public void setService(NashornObjectService service) {
        this.service = service;
    }
}
