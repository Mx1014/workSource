//
// EvhActivityCheckinCommand.h
// generated at 2016-04-06 19:59:45 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityCheckinCommand
//
@interface EvhActivityCheckinCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* activityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

