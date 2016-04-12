//
// EvhOrgListUserRelatedEnterprisesRestResponse.h
// generated at 2016-04-08 20:09:24 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListUserRelatedEnterprisesRestResponse
//
@interface EvhOrgListUserRelatedEnterprisesRestResponse : EvhRestResponseBase

// array of EvhOrganizationDetailDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
