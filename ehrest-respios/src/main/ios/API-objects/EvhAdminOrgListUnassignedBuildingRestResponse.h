//
// EvhAdminOrgListUnassignedBuildingRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListUnassignedBuildingRestResponse
//
@interface EvhAdminOrgListUnassignedBuildingRestResponse : EvhRestResponseBase

// array of EvhUnassignedBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
