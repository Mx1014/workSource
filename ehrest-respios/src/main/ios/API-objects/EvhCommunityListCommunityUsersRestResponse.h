//
// EvhCommunityListCommunityUsersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityListCommunityUsersRestResponse
//
@interface EvhCommunityListCommunityUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
