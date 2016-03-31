//
// EvhChannel.h
// generated at 2016-03-31 13:49:14 
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

