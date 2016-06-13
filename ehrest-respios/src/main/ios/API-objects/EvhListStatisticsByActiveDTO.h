//
// EvhListStatisticsByActiveDTO.h
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

