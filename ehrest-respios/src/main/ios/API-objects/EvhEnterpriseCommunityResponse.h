//
// EvhEnterpriseCommunityResponse.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseCommunityResponse
//
@interface EvhEnterpriseCommunityResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseCommunityDTO*
@property(nonatomic, strong) NSMutableArray* communities;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

