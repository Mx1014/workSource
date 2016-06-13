//
// EvhScheduleCronJobCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleCronJobCommand
//
@interface EvhScheduleCronJobCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* triggerName;

@property(nonatomic, copy) NSString* jobName;

@property(nonatomic, copy) NSString* cronExpression;

@property(nonatomic, copy) NSString* jobClassName;

@property(nonatomic, copy) NSString* parameterJson;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

