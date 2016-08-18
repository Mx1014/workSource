package com.everhomes.richtext;


import com.everhomes.rest.richtext.GetRichTextByTokenCommand;
import com.everhomes.rest.richtext.GetRichTextCommand;
import com.everhomes.rest.richtext.RichTextDTO;
import com.everhomes.rest.richtext.UpdateRichTextCommand;

public interface RichTextService {
	
	void updateRichText(UpdateRichTextCommand cmd);
	RichTextDTO getRichText(GetRichTextCommand cmd);
	RichTextDTO getRichTextByToken(GetRichTextByTokenCommand cmd);

}
