//
// EvhAdminOrgListPmManagementsRestResponse.h
// generated at 2016-03-30 10:13:09 
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
