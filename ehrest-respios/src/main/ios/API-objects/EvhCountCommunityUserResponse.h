//
// EvhCountCommunityUserResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCountCommunityUserResponse
//
@interface EvhCountCommunityUserResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityUsers;

@property(nonatomic, copy) NSNumber* authUsers;

@property(nonatomic, copy) NSNumber* notAuthUsers;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

