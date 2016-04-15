//
// EvhAdminCommunityGetUserCommunitiesRestResponse.h
// generated at 2016-04-12 15:02:20 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityGetUserCommunitiesRestResponse
//
@interface EvhAdminCommunityGetUserCommunitiesRestResponse : EvhRestResponseBase

// array of EvhUserCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
