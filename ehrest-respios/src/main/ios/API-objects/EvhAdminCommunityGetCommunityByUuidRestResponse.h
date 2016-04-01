//
// EvhAdminCommunityGetCommunityByUuidRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityGetCommunityByUuidRestResponse
//
@interface EvhAdminCommunityGetCommunityByUuidRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
