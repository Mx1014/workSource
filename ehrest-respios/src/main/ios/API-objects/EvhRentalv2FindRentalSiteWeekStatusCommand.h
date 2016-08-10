//
// EvhRentalv2FindRentalSiteWeekStatusCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSiteWeekStatusCommand
//
@interface EvhRentalv2FindRentalSiteWeekStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* siteId;

@property(nonatomic, copy) NSNumber* ruleDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

