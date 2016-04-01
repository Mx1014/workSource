//
// EvhListOrganizationCommunityV2CommandResponse.h
// generated at 2016-04-01 15:40:22 
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

