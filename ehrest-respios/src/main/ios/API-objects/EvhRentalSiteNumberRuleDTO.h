//
// EvhRentalSiteNumberRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteNumberRuleDTO
//
@interface EvhRentalSiteNumberRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* siteNumber;

// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

