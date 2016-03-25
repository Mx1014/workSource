//
// EvhUserListUsersInCurrentCommunityRestResponse.h
// generated at 2016-03-25 11:43:35 
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
