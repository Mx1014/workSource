//
// EvhChannel.h
// generated at 2016-03-25 15:57:23 
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

