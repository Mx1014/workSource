//
// EvhRentalRentalSiteDayRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalRentalSiteDayRulesDTO
//
@interface EvhRentalRentalSiteDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

