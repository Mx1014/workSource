//
// EvhOpenMsgSessionActionData.h
// generated at 2016-04-07 15:16:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenMsgSessionActionData
//
@interface EvhOpenMsgSessionActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* dstChannel;

@property(nonatomic, copy) NSNumber* dstChannelId;

@property(nonatomic, copy) NSString* srcChannel;

@property(nonatomic, copy) NSNumber* srcChannelId;

@property(nonatomic, copy) NSNumber* senderUid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

