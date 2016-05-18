//
// EvhScheduleAtTimeCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleAtTimeCommand
//
@interface EvhScheduleAtTimeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* triggerName;

@property(nonatomic, copy) NSString* jobName;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* jobClassName;

@property(nonatomic, copy) NSString* parameterJson;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

