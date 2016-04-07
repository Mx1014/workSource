//
// EvhAdminOrgListPmBuildingsRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListPmBuildingsRestResponse
//
@interface EvhAdminOrgListPmBuildingsRestResponse : EvhRestResponseBase

// array of EvhPmBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
