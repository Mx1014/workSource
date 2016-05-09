//
// EvhOrgGetCurrentOrganizationRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgGetCurrentOrganizationRestResponse
//
@interface EvhOrgGetCurrentOrganizationRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
