//
// EvhListNearbyMixCommunitiesCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyMixCommunitiesCommandResponse
//
@interface EvhListNearbyMixCommunitiesCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhCommunityDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

