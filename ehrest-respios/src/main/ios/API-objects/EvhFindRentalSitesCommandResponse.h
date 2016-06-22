//
// EvhFindRentalSitesCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesCommandResponse
//
@interface EvhFindRentalSitesCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRentalSiteDTO*
@property(nonatomic, strong) NSMutableArray* rentalSites;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

