//
// EvhRentalRentalSiteDayRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalRentalSiteDayRulesDTO
//
@interface EvhRentalRentalSiteDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

