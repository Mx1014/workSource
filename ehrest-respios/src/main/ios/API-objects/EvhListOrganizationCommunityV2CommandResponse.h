//
// EvhListOrganizationCommunityV2CommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationCommunityV2CommandResponse
//
@interface EvhListOrganizationCommunityV2CommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhCommunityDTO*
@property(nonatomic, strong) NSMutableArray* communities;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

