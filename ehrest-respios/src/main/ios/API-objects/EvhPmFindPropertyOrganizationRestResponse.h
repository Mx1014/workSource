//
// EvhPmFindPropertyOrganizationRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindPropertyOrganizationRestResponse
//
@interface EvhPmFindPropertyOrganizationRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
