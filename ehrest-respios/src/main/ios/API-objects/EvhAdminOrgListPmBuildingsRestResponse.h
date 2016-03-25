//
// EvhAdminOrgListPmBuildingsRestResponse.h
// generated at 2016-03-25 11:43:34 
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
