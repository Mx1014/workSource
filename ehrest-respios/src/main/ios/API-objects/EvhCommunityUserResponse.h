//
// EvhCommunityUserResponse.h
// generated at 2016-04-08 20:09:23 
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

