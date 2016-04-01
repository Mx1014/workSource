//
// EvhCommunityListCommunityUsersRestResponse.h
// generated at 2016-03-31 20:15:33 
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
