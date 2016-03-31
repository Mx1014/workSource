//
// EvhAdminCommunityListBuildingsByStatusRestResponse.h
// generated at 2016-03-31 13:49:15 
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
