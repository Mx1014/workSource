//
// EvhUserListUsersInCurrentCommunityRestResponse.h
// generated at 2016-04-22 13:56:52 
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
