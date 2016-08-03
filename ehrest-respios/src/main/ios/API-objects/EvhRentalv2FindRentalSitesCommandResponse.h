//
// EvhRentalv2FindRentalSitesCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSitesCommandResponse
//
@interface EvhRentalv2FindRentalSitesCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRentalv2RentalSiteDTO*
@property(nonatomic, strong) NSMutableArray* rentalSites;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

