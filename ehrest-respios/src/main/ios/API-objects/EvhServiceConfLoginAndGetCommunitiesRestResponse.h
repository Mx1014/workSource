//
// EvhServiceConfLoginAndGetCommunitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhServiceConfLoginAndGetCommunitiesRestResponse
//
@interface EvhServiceConfLoginAndGetCommunitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
