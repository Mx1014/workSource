//
// EvhRentalSiteNumberDayRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteNumberRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteNumberDayRulesDTO
//
@interface EvhRentalSiteNumberDayRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalDate;

// item type EvhRentalSiteNumberRuleDTO*
@property(nonatomic, strong) NSMutableArray* siteNumbers;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

