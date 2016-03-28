//
// EvhCountCommunityUserResponse.h
// generated at 2016-03-25 19:05:19 
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

