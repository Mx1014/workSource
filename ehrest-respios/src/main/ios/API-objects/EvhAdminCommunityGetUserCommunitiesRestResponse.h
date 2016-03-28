//
// EvhAdminCommunityGetUserCommunitiesRestResponse.h
// generated at 2016-03-28 15:56:09 
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
