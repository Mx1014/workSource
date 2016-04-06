//
// EvhCommunityUserResponse.h
// generated at 2016-04-06 19:59:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityUserDto.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserResponse
//
@interface EvhCommunityUserResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCommunityUserDto*
@property(nonatomic, strong) NSMutableArray* userCommunities;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

