//
// EvhRentalFindRentalSiteRulesCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteRulesCommandResponse
//
@interface EvhRentalFindRentalSiteRulesCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* rentalSiteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

