//
// EvhGetResourceListAdminResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetResourceListAdminResponse
//
@interface EvhGetResourceListAdminResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhBuildingDTO*
@property(nonatomic, strong) NSMutableArray* rentalSites;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

