//
// EvhAddNewsCommentCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNewsAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddNewsCommentCommand
//
@interface EvhAddNewsCommentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

// item type EvhNewsAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

