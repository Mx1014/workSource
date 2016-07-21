//
// EvhRentalv2FindRentalSiteRulesCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSiteRulesCommandResponse
//
@interface EvhRentalv2FindRentalSiteRulesCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2RentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* rentalSiteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

