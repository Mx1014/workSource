//
// EvhAddNewsCommentBySceneCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddNewsCommentBySceneCommand
//
@interface EvhAddNewsCommentBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* contentType;

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

