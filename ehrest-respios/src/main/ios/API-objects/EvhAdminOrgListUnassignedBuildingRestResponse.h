//
// EvhAdminOrgListUnassignedBuildingRestResponse.h
// generated at 2016-04-12 15:02:21 
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
