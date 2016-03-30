//
// EvhUserListUsersInCurrentCommunityRestResponse.h
// generated at 2016-03-30 10:13:10 
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
