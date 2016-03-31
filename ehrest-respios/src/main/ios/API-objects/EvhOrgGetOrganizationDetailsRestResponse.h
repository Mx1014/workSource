//
// EvhOrgGetOrganizationDetailsRestResponse.h
// generated at 2016-03-31 15:43:24 
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
