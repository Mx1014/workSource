//
// EvhMessageChannel.h
// generated at 2016-04-07 10:47:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMessageChannel
//
@interface EvhMessageChannel
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* channelType;

@property(nonatomic, copy) NSString* channelToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

