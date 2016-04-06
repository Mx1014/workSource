//
// EvhAdminOrgListPmManagementsRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhPmManagementsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListPmManagementsRestResponse
//
@interface EvhAdminOrgListPmManagementsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmManagementsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
