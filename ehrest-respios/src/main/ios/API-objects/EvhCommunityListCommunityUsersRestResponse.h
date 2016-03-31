//
// EvhCommunityListCommunityUsersRestResponse.h
// generated at 2016-03-31 13:49:15 
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
