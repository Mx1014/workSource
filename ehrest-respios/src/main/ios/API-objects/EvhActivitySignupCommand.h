//
// EvhActivitySignupCommand.h
// generated at 2016-03-31 20:15:32 
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

