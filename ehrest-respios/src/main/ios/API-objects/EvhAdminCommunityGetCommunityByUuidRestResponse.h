//
// EvhAdminCommunityGetCommunityByUuidRestResponse.h
// generated at 2016-04-07 15:16:53 
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
