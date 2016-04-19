//
// EvhScheduleRepeatJobCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleRepeatJobCommand
//
@interface EvhScheduleRepeatJobCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* triggerName;

@property(nonatomic, copy) NSString* jobName;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSNumber* repeatInterval;

@property(nonatomic, copy) NSNumber* repeatCount;

@property(nonatomic, copy) NSString* jobClassName;

@property(nonatomic, copy) NSString* parameterJson;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

