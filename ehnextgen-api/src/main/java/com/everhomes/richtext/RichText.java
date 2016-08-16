package com.everhomes.richtext;

import com.everhomes.server.schema.tables.pojos.EhRichTexts;
import com.everhomes.util.StringHelper;

public class RichText extends EhRichTexts {

	private static final long serialVersionUID = -3924644100522686606L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
