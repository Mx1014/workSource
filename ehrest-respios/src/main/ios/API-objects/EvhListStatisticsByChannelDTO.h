//
// EvhListStatisticsByChannelDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

