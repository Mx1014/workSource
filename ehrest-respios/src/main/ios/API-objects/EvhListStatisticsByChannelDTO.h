//
// EvhListStatisticsByChannelDTO.h
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

