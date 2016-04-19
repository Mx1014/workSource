//
// EvhAdminOrgListPmManagementsRestResponse.h
// generated at 2016-04-19 12:41:54 
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
