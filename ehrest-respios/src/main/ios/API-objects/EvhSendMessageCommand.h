//
// EvhSendMessageCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhMessageChannel.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageCommand
//
@interface EvhSendMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* deliveryOption;

@property(nonatomic, copy) NSNumber* senderUid;

@property(nonatomic, copy) NSString* contextType;

@property(nonatomic, copy) NSString* contextToken;

// item type EvhMessageChannel*
@property(nonatomic, strong) NSMutableArray* channels;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* meta;

@property(nonatomic, copy) NSString* bodyType;

@property(nonatomic, copy) NSString* body;

@property(nonatomic, copy) NSString* senderTag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

