//
// EvhUserListUsersInCurrentCommunityRestResponse.h
// generated at 2016-03-28 15:56:09 
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
