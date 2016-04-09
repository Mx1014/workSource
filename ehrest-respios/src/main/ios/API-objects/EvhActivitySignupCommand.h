//
// EvhActivitySignupCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivitySignupCommand
//
@interface EvhActivitySignupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* activityId;

@property(nonatomic, copy) NSNumber* adultCount;

@property(nonatomic, copy) NSNumber* childCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

