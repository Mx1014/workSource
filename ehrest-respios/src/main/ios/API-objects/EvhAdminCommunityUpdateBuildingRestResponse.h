//
// EvhAdminCommunityUpdateBuildingRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhCommunityBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityUpdateBuildingRestResponse
//
@interface EvhAdminCommunityUpdateBuildingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityBuildingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
