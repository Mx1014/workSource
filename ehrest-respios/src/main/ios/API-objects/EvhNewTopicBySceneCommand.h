//
// EvhNewTopicBySceneCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewTopicBySceneCommand
//
@interface EvhNewTopicBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* targetTag;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* embeddedAppId;

@property(nonatomic, copy) NSNumber* embeddedId;

@property(nonatomic, copy) NSString* embeddedJson;

@property(nonatomic, copy) NSNumber* isForwarded;

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* privateFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

