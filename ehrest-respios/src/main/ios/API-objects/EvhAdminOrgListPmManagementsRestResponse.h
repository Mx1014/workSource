//
// EvhAdminOrgListPmManagementsRestResponse.h
// generated at 2016-04-19 14:25:57 
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
