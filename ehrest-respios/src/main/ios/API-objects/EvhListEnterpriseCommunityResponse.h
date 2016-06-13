//
// EvhListEnterpriseCommunityResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseCommunityResponse
//
@interface EvhListEnterpriseCommunityResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseCommunityDTO*
@property(nonatomic, strong) NSMutableArray* enterpriseCommunities;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

