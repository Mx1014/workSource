package com.everhomes.flow;
import com.everhomes.news.Attachment;
import com.everhomes.server.schema.tables.pojos.EhFlowSubjects;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class FlowSubject extends EhFlowSubjects {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5020988711127444590L;

	// 按钮触发时上传的附件
    private List<Attachment> attachments = new ArrayList<>();

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
