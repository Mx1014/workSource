//
// EvhExportPunchStatisticsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExportPunchStatisticsCommand
//
@interface EvhExportPunchStatisticsCommand
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

