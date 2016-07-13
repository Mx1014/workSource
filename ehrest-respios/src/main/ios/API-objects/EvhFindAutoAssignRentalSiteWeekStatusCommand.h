//
// EvhFindAutoAssignRentalSiteWeekStatusCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindAutoAssignRentalSiteWeekStatusCommand
//
@interface EvhFindAutoAssignRentalSiteWeekStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* siteId;

@property(nonatomic, copy) NSNumber* ruleDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

