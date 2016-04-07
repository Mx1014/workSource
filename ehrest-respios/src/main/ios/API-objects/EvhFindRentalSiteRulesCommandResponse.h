//
// EvhFindRentalSiteRulesCommandResponse.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteRulesCommandResponse
//
@interface EvhFindRentalSiteRulesCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* rentalSiteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

