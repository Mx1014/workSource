//
// EvhAdminCommunityListCommunityByNamespaceIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunityByNamespaceIdResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListCommunityByNamespaceIdRestResponse
//
@interface EvhAdminCommunityListCommunityByNamespaceIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunityByNamespaceIdResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
