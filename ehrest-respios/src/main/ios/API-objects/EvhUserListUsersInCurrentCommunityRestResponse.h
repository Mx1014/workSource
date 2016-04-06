//
// EvhUserListUsersInCurrentCommunityRestResponse.h
// generated at 2016-04-06 19:10:44 
//
#import "RestResponseBase.h"
#import "EvhCommunityStatusResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListUsersInCurrentCommunityRestResponse
//
@interface EvhUserListUsersInCurrentCommunityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityStatusResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
