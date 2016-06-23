package com.everhomes.news;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;

public class AttachmentTest extends CoreServerTestCase{
	
	@Autowired
	private AttachmentProvider attachmentProvider;
	
	@Test
	public void testListAttachmentByOwnerIds(){
		System.err.println("begin++++++++++++++++++++++++++++++++++++++++++++++");
		attachmentProvider.listAttachmentByOwnerIds(EhNewsAttachments.class, Arrays.asList(2L,3L)).forEach(a->System.err.println(a));
		System.err.println("end++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
}
