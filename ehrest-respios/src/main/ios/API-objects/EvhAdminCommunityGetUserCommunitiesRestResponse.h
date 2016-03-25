//
// EvhAdminCommunityGetUserCommunitiesRestResponse.h
// generated at 2016-03-25 17:08:12 
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
