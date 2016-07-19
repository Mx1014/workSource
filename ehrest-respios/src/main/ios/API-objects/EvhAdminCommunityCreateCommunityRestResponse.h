//
// EvhAdminCommunityCreateCommunityRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCreateCommunityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityCreateCommunityRestResponse
//
@interface EvhAdminCommunityCreateCommunityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCreateCommunityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
