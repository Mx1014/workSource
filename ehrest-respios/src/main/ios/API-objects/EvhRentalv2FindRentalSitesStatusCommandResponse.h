//
// EvhRentalv2FindRentalSitesStatusCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSitesStatusCommandResponse
//
@interface EvhRentalv2FindRentalSitesStatusCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2RentalSiteDTO*
@property(nonatomic, strong) NSMutableArray* sites;

@property(nonatomic, copy) NSString* contactNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

