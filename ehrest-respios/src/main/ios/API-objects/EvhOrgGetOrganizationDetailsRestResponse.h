//
// EvhOrgGetOrganizationDetailsRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgGetOrganizationDetailsRestResponse
//
@interface EvhOrgGetOrganizationDetailsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
