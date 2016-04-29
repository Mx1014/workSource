//
// EvhScheduleJobInfoDTO.h
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

