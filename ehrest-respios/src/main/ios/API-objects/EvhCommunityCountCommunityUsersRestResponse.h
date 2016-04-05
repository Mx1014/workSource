//
// EvhCommunityCountCommunityUsersRestResponse.h
// generated at 2016-04-05 13:45:27 
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
