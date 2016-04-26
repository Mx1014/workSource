//
// EvhScheduleRepeatJobCommand.h
// generated at 2016-04-26 18:22:54 
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

