//
// EvhListStatisticsByActiveDTO.h
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
// EvhListStatisticsByActiveDTO
//
@interface EvhListStatisticsByActiveDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* yesterdayActiveCount;

@property(nonatomic, copy) NSNumber* lastWeekActiveCount;

@property(nonatomic, copy) NSNumber* lastMonthActiveCount;

@property(nonatomic, copy) NSNumber* ystToLastWeekRatio;

@property(nonatomic, copy) NSNumber* ystToLastMonthRatio;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* activeCount;

@property(nonatomic, copy) NSNumber* dayActiveToSearchRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

