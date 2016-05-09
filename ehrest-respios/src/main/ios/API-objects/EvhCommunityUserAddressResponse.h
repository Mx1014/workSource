//
// EvhCommunityUserAddressResponse.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityUserAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserAddressResponse
//
@interface EvhCommunityUserAddressResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCommunityUserAddressDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

