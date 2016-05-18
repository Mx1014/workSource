//
// EvhListPunchStatisticsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchStatisticsCommand
//
@interface EvhListPunchStatisticsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSString* startDay;

@property(nonatomic, copy) NSString* endDay;

@property(nonatomic, copy) NSNumber* arriveTimeCompareFlag;

@property(nonatomic, copy) NSString* arriveTime;

@property(nonatomic, copy) NSNumber* leaveTimeCompareFlag;

@property(nonatomic, copy) NSString* leaveTime;

@property(nonatomic, copy) NSNumber* workTimeCompareFlag;

@property(nonatomic, copy) NSString* workTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* enterpriseGroupId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

