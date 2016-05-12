//
// EvhAdminCommunityListUnassignedCommunitiesByNamespaceIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListUnassignedCommunitiesByNamespaceIdRestResponse
//
@interface EvhAdminCommunityListUnassignedCommunitiesByNamespaceIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
