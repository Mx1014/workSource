//
// EvhScheduleJobInfoDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleJobInfoDTO
//
@interface EvhScheduleJobInfoDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* triggerGroupName;

@property(nonatomic, copy) NSString* triggerName;

@property(nonatomic, copy) NSString* triggerType;

@property(nonatomic, copy) NSString* triggerState;

@property(nonatomic, copy) NSString* jobGroupName;

@property(nonatomic, copy) NSString* jobName;

@property(nonatomic, copy) NSString* cronExpression;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* endTime;

@property(nonatomic, copy) NSString* previousFireTime;

@property(nonatomic, copy) NSString* nextFireTime;

@property(nonatomic, copy) NSString* finalFireTime;

@property(nonatomic, copy) NSNumber* misfireInstruction;

@property(nonatomic, copy) NSNumber* repeatInterval;

@property(nonatomic, copy) NSNumber* repeatCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

