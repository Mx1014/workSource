//
// EvhAdminCommunityUpdateBuildingRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityUpdateBuildingRestResponse
//
@interface EvhAdminCommunityUpdateBuildingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBuildingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
