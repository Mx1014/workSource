//
// EvhOrgListEnterprisesRestResponse.h
// generated at 2016-04-12 15:02:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListEnterprisesRestResponse
//
@interface EvhOrgListEnterprisesRestResponse : EvhRestResponseBase

// array of EvhOrganizationDetailDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
