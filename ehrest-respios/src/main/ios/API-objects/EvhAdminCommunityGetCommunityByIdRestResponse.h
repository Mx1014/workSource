//
// EvhAdminCommunityGetCommunityByIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityGetCommunityByIdRestResponse
//
@interface EvhAdminCommunityGetCommunityByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
