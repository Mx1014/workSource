//
// EvhAdminOrgListPmManagementComunitesRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListPmManagementComunitesRestResponse
//
@interface EvhAdminOrgListPmManagementComunitesRestResponse : EvhRestResponseBase

// array of EvhPmManagementCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
