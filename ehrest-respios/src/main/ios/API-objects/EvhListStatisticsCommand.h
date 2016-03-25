//
// EvhListStatisticsCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsCommand
//
@interface EvhListStatisticsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* stopTime;

@property(nonatomic, copy) NSNumber* channelId;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

