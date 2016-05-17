//
// EvhOpenapiGetCommunityByIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetCommunityByIdRestResponse
//
@interface EvhOpenapiGetCommunityByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
