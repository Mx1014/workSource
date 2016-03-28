//
// EvhRentalSiteDayRulesDTO.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDayRulesDTO
//
@interface EvhRentalSiteDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

