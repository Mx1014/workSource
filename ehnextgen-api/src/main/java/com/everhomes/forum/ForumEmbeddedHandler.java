// @formatter:off
package com.everhomes.forum;

public interface ForumEmbeddedHandler {
    String FORUM_EMBEDED_OBJ_RESOLVER_PREFIX = "ForumEmbededApp-";
    
    String renderEmbeddedObjectSnapshot(Post post);
    String renderEmbeddedObjectDetails(Post post);
    Post preProcessEmbeddedObject(Post post);
    Post postProcessEmbeddedObject(Post post);
}
