//
// EvhRentalv2DeleteRentalSiteRulesCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2DeleteRentalSiteRulesCommand
//
@interface EvhRentalv2DeleteRentalSiteRulesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseCommunityId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSString* rentalSiteId;

@property(nonatomic, copy) NSString* ruleDates;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

