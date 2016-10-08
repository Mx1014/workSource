package com.everhomes.forum;


import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.ui.user.ContentBriefDTO;

public interface ForumFootnoteHandler {
	String FORUM_FOOTNOTE_RESOLVER_PREFIX = "ForumFootnote-";
    
    String renderContentFootnote(ContentBriefDTO dto, PostDTO postDto);
}
