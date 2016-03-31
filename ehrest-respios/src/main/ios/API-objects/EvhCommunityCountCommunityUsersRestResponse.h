//
// EvhCommunityCountCommunityUsersRestResponse.h
// generated at 2016-03-28 15:56:09 
//
#import "RestResponseBase.h"
#import "EvhCountCommunityUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityCountCommunityUsersRestResponse
//
@interface EvhCommunityCountCommunityUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCountCommunityUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
