//
// EvhCommunityCountCommunityUsersRestResponse.h
// generated at 2016-04-07 17:33:49 
//
#import "RestResponseBase.h"
#import "EvhCountCommunityUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityCountCommunityUsersRestResponse
//
@interface EvhCommunityCountCommunityUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCountCommunityUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
