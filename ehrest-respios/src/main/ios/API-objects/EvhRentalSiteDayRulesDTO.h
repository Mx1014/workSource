//
// EvhRentalSiteDayRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDayRulesDTO
//
@interface EvhRentalSiteDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

