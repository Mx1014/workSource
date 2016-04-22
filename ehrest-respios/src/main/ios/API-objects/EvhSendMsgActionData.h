//
// EvhSendMsgActionData.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMsgActionData
//
@interface EvhSendMsgActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* dstChannel;

@property(nonatomic, copy) NSNumber* dstChannelId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

