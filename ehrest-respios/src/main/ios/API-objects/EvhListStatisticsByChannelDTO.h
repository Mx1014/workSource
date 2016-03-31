//
// EvhListStatisticsByChannelDTO.h
// generated at 2016-03-31 11:07:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByChannelDTO
//
@interface EvhListStatisticsByChannelDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* channel;

@property(nonatomic, copy) NSNumber* activeCount;

@property(nonatomic, copy) NSNumber* registerConut;

@property(nonatomic, copy) NSNumber* regRatio;

@property(nonatomic, copy) NSNumber* channelActiveRatio;

@property(nonatomic, copy) NSNumber* channelRegRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

