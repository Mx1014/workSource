//
// EvhAdminOrgListPmManagementsRestResponse.h
// generated at 2016-03-31 10:18:21 
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
