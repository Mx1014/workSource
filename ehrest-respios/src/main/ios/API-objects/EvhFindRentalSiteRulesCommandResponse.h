//
// EvhFindRentalSiteRulesCommandResponse.h
// generated at 2016-03-28 15:56:08 
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

