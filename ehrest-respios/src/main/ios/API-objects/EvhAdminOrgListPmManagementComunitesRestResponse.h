//
// EvhAdminOrgListPmManagementComunitesRestResponse.h
// generated at 2016-04-06 19:10:43 
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
