//
// EvhCommunityListCommunityUsersRestResponse.h
// generated at 2016-04-18 14:48:52 
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
