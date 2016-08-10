//
// EvhServiceConfListCommunityServicesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunityServiceResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhServiceConfListCommunityServicesRestResponse
//
@interface EvhServiceConfListCommunityServicesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunityServiceResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
