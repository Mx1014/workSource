package com.everhomes.flow;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhFlowCasesRecord;

public class FlowCaseRecordMapper implements RecordMapper<Record, EhFlowCasesRecord> {

    @SuppressWarnings("unchecked")
    @Override
    public EhFlowCasesRecord map(Record r) {
	    	EhFlowCasesRecord o = new EhFlowCasesRecord();
			o.setId(r.getValue((Field<Long>)r.field("id")));
			o.setNamespaceId(r.getValue((Field<Integer>)r.field("namespace_id")));
			o.setOwnerId(r.getValue((Field<Long>)r.field("owner_id")));
			o.setOwnerType(r.getValue((Field<String>)r.field("owner_type")));
			o.setModuleId(r.getValue((Field<Long>)r.field("module_id")));
			o.setModuleType(r.getValue((Field<String>)r.field("module_type")));
			o.setModuleName(r.getValue((Field<String>)r.field("module_name")));
			o.setApplierName(r.getValue((Field<String>)r.field("applier_name")));
			o.setApplierPhone(r.getValue((Field<String>)r.field("applier_phone")));
			o.setFlowMainId(r.getValue((Field<Long>)r.field("flow_main_id")));
			o.setFlowVersion(r.getValue((Field<Integer>)r.field("flow_version")));
			o.setApplyUserId(r.getValue((Field<Long>)r.field("apply_user_id")));
			o.setProcessUserId(r.getValue((Field<Long>)r.field("process_user_id")));
			o.setReferId(r.getValue((Field<Long>)r.field("refer_id")));
			o.setReferType(r.getValue((Field<String>)r.field("refer_type")));
			o.setCurrentNodeId(r.getValue((Field<Long>)r.field("current_node_id")));
			o.setStatus(r.getValue((Field<Byte>)r.field("status")));
			o.setRejectCount(r.getValue((Field<Integer>)r.field("reject_count")));
			o.setRejectNodeId(r.getValue((Field<Long>)r.field("reject_node_id")));
			o.setStepCount(r.getValue((Field<Long>)r.field("step_count")));
			o.setLastStepTime(r.getValue((Field<Timestamp>)r.field("last_step_time")));
			o.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
			o.setCaseType(r.getValue((Field<String>)r.field("case_type")));
			o.setContent(r.getValue((Field<String>)r.field("content")));
			o.setEvaluateScore(r.getValue((Field<Integer>)r.field("evaluate_score")));
			o.setTitle(r.getValue((Field<String>)r.field("title")));
			o.setStringTag1(r.getValue((Field<String>)r.field("string_tag1")));
			o.setStringTag2(r.getValue((Field<String>)r.field("string_tag2")));
			o.setStringTag3(r.getValue((Field<String>)r.field("string_tag3")));
			o.setStringTag4(r.getValue((Field<String>)r.field("string_tag4")));
			o.setStringTag5(r.getValue((Field<String>)r.field("string_tag5")));
			o.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag1")));
			o.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag2")));
			o.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag3")));
			o.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag4")));
			o.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag5")));
			return o;
		}
}
