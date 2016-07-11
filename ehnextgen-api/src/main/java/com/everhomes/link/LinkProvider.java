// @formatter:off
package com.everhomes.link;



public interface LinkProvider {
    void createLink(Link link);
    void updateLink(Link link);
    void deleteLink(Link link);
    void deleteLinkById(Long id);
    Link findLinkById(Long id);

    
    Link findLinkByPostId(Long postId);

}
