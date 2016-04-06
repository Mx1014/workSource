//
// EvhChannel.h
// generated at 2016-04-06 19:59:44 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhChannel
//
@interface EvhChannel
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* channelName;

@property(nonatomic, copy) NSNumber* channelId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

