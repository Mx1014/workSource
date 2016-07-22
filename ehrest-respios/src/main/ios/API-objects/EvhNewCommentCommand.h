//
// EvhNewCommentCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewCommentCommand
//
@interface EvhNewCommentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* embeddedAppId;

@property(nonatomic, copy) NSNumber* embeddedId;

@property(nonatomic, copy) NSString* embeddedJson;

// item type EvhForumAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

