//
// EvhOrgSearchEnterpriseRestResponse.h
// generated at 2016-04-29 18:56:04 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgSearchEnterpriseRestResponse
//
@interface EvhOrgSearchEnterpriseRestResponse : EvhRestResponseBase

// array of EvhOrganizationDetailDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
