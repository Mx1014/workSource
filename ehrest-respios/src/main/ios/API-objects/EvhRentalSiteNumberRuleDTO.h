//
// EvhRentalSiteNumberRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteNumberRuleDTO
//
@interface EvhRentalSiteNumberRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* siteNumber;

// item type EvhRentalv2RentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

