//
// EvhActivityCancelSignupCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityCancelSignupCommand
//
@interface EvhActivityCancelSignupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* activityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

