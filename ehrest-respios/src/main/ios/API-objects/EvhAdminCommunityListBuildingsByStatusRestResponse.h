//
// EvhAdminCommunityListBuildingsByStatusRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListBuildingsByStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListBuildingsByStatusRestResponse
//
@interface EvhAdminCommunityListBuildingsByStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBuildingsByStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
