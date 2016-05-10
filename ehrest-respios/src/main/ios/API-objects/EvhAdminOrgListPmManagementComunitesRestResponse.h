//
// EvhAdminOrgListPmManagementComunitesRestResponse.h
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
