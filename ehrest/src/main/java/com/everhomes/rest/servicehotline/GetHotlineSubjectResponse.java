package com.everhomes.rest.servicehotline;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * 
 * <li>subjects: 标签页{@link HotlineSubject}</li>  
 * </ul>
 */
public class GetHotlineSubjectResponse {
	@ItemType(HotlineSubject.class)
	private List<HotlineSubject> subjects;
	 
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public List<HotlineSubject> getSubjects() {
		return subjects;
	}


	public void setSubjects(List<HotlineSubject> subjects) {
		this.subjects = subjects;
	}

 
}
