//
// EvhChannel.h
// generated at 2016-03-25 17:08:10 
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

