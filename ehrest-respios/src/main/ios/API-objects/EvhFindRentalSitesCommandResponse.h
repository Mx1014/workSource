//
// EvhFindRentalSitesCommandResponse.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesCommandResponse
//
@interface EvhFindRentalSitesCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhRentalSiteDTO*
@property(nonatomic, strong) NSMutableArray* rentalSites;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

