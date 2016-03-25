//
// EvhListEnterpriseCommunityResponse.h
// generated at 2016-03-25 09:26:41 
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

