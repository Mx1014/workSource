//
// EvhOrgGetCurrentOrganizationRestResponse.h
// generated at 2016-03-28 15:56:09 
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
