//
// EvhRentalv2RentalSiteDayRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2RentalSiteDayRulesDTO
//
@interface EvhRentalv2RentalSiteDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalv2RentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

