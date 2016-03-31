//
// EvhMessageChannel.h
// generated at 2016-03-31 19:08:53 
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

