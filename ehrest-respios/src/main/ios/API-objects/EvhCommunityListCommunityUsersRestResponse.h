//
// EvhCommunityListCommunityUsersRestResponse.h
// generated at 2016-04-08 20:09:23 
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
