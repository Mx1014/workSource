//
// EvhAdminCommunityUpdateBuildingRestResponse.h
// generated at 2016-03-25 11:43:34 
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
