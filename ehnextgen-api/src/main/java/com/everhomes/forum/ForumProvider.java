package com.everhomes.forum;

import java.util.Comparator;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

/**
 * ForumProvider defines server-side forum API
 * 
 * Posts and their replied posts are logically organized in order, an anchor mechanism 
 * is used for pagination in listing.
 * 
 * Listing APIs (listXXXX) are aware of Redis storage, while Query APIs (queryXXXX) only deal with
 * Database queries
 * 
 * @author Kelven Yang
 *
 */
public interface ForumProvider {
    /**
     * Create a forum record in storage system (Redis/DB)
     * 
     * @param forum new record that contains all necessary information except those that
     * storage system will fill after the record is stored 
     * 
     */
    void createForum(Forum forum);
    
    /**
     * Update forum object to storage system
     * 
     * @param forum a fully filled forum record that contains the update information. For fields
     * that are not changed, they should contains the more recent content from storage system
     *  
     */
    void updateForum(Forum forum);
    
    void deleteForum(Forum forum);

    /**
     * Delete forum object from storage system
     * 
     * @param id forum id
     */
    void deleteForum(long id);
    
    /**
     * Retrieve the forum object from storage system
     * 
     * @param id forum id
     * 
     * @return the retrieved forum object
     */
    Forum findForumById(long id);
    
    /**
     * Retrieve the forum object from storage system
     * 
     * @param name forum name
     * 
     * @return the retrieved forum object
     */
    Forum findForumByName(String name);
    
    /**
     * Retrieve the forum object by specific <code>ownerType</code> and <code>ownerId</code>
     * @param ownerType {@link com.everhomes.entity.EntityType}
     * @param ownerId the id according to <code>ownerType</code>
     * @return forum object list
     */
    List<Forum> findForumByOwner(final String ownerType, final long ownerId);
        
    /**
     * 
     * Create a forum post record
     * 
     * @param post record that contains all necessary information except those that
     * storage system will fill after the record is stored. Nested information about
     * replying post and attachments are not processed. Caller is not responsible to 
     * fill anchor info
     * 
     */
    void createPost(Post post);
    
    /**
     * Retrieve a post record from storage system
     * 
     * @param postId identifies the post record
     * @return the retrieved record object, the object does not contain nested information 
     * of replying posts, but it contains nested attachment information
     */
    Post findPostById(long postId);
    
    /**
     * Retrieve a post record from storage system
     * 
     * @param uuid identifies the post record
     * @return the retrieved record object, the object does not contain nested information 
     * of replying posts, but it contains nested attachment information
     */
    Post findPostByUuid(String uuid);
    
    /**
     * Update a post record
     * 
     * @param post record that contains the updated information
     */
    void updatePost(Post post);
    
    /**
     * Delete a post record from storage system, if it contains replying child posts, they will
     * be deleted too.
     * 
     * @param postId identifies the post record
     */
    void deletePost(long postId);
    
    /**
     * Create a post attachment record
     * 
     * @param attachment record that contains the content
     */
    void createPostAttachment(Attachment attachment);
    
    /**
     * Retrieve the attachment object from storage system
     * 
     * @param id identifies the record
     * @return retrieved object
     */
    Attachment findAttachmentById(long id);
    
    /**
     * Update attachment information
     * 
     * @param attachment the attachment record
     */
    void updateAttachment(Attachment attachment);
    
    void deleteAttachment(Attachment attachment);
    
    /**
     * Delete attachment record from storage
     * 
     * @param id
     */
    void deleteAttachment(long id);
    
    /**
     * 
     * Retrieve all attachments of a forum post
     * @param postId
     * @return
     */
    List<Attachment> listPostAttachments(long postId);
    
    /**
     * Give a like vote to the post
     * 
     * @param uid user identifier
     * @param postId identifies the post object
     */
    void likePost(long uid, long postId);
    
    /**
     * Give a dislike vote
     * 
     * @param uid user identifier
     * @param postId identifies the post object
     */
    void cancelLikePost(long uid, long postId);
    
    /**
     * No use now
     * @param postId
     * @param uid
     */
    void dislikePost(long postId, long uid);
    
    /**
     * Cancel previous like/dislike vote if exists
     * 
     * @param postId identifies the post object
     * @param uid user id
     */
    void cancelLikeDislikePost(long postId, long uid);
    
    /**
     * Query forum items with customized query criteria, query result from multiple forums will be
     * merged based on given comparable parameter. Recent forum items in Redis will not be searched,
     * results are only retrieved from DB
     * 
     * @param forums array of forum location info, upon return, location information is updated for next iteration
     * @param maxRecentReplyingPosts maximum number of recent child replying posts to retrieve
     * @param count number of the items to retrieve for current iteration
     * @param queryBuilderCallback callback to help setup customized query conditions
     * @param mergeCamparable comparable used in merging multiple-forum results
     * @return merged and sorted results
     */
    List<Post> queryPosts(CrossShardListingLocator forums[], int maxRecentReplyingPosts, int count, 
            ListingQueryBuilderCallback queryBuilderCallback,
            Comparator<Post> mergeCamparable);
    
    List<Post> queryPosts(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    
    /**
     * Query forum replying posts with customized query criteria. Recent items in Redis will not be searched,
     * results are only retrieved from DB
     * 
     * @param locator specifies the anchor point, upon return, anchor location is updated for next iteration
     * @param count number of the items to retrieve for current iteration 
     * @param queryBuilderCallback callback to help setup customized query conditions
     * @return replying post
     */
    List<Post> queryReplyingPosts(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    
    /**
     * <p>Iterate all post by pagination, the <code>count</code> indicate the page count;
     * While iterating the post, it will query posts page by page until to the end;
     * It can specify the querying condition by <code>queryBuilderCallback</code>;
     * After querying, every item will be processed by the <code>callback</code>.</p>
     * 
     * @param count page count
     * @param callback the processing callback
     * @param queryBuilderCallback querying condition callback
     */
    void iteratePosts(int count, IteratePostCallback callback, ListingQueryBuilderCallback queryBuilderCallback);
    
    /**
     * Query the post attachements and populate them to the post. 
     * @param posts
     */
    void populatePostAttachments(final Post post);
    
    /**
     * Query the post attachements and populate them to the posts. 
     * @param posts
     */
    void populatePostAttachments(final List<Post> posts);
    
    /**
     * Create the assigned scope for the post, which may extend the scope of post
     * @param scope 
     */
    void createAssignedScope(AssignedScope scope);
    
    /**
     * Find the assigned scope by the specific <code>id</code>
     * @param id
     * @return
     */
    AssignedScope findAssignedScopeById(Long id);
    
    /**
     * Find all the assigned scope whose owner is specified by <code>ownerId</code>
     * @param ownerId
     * @return
     */
    List<AssignedScope> findAssignedScopeByOwnerId(Long ownerId);
    
    /**
     * Delete the specific assigned scope
     * @param scope
     */
    void deleteAssignedScope(AssignedScope scope);
    
    /**
     * Delete the assigned scope by the specific <code>id</code>
     * @param id
     */
    void deleteAssignedScopeById(Long id);
    
    void modifyHotPost();

	List<Post> listForumPostByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<Post> listForumPostByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<Post> listForumCommentByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<Post> listForumCommentByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);
}
