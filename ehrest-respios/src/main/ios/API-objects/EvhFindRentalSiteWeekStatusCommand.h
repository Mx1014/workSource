//
// EvhFindRentalSiteWeekStatusCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteWeekStatusCommand
//
@interface EvhFindRentalSiteWeekStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* siteId;

@property(nonatomic, copy) NSNumber* ruleDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

