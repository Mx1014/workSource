//
// EvhOrgGetCurrentOrganizationRestResponse.h
// generated at 2016-04-05 13:45:27 
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
