//
// EvhAdminOrgVerifyPersonnelByPhoneRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgVerifyPersonnelByPhoneRestResponse
//
@interface EvhAdminOrgVerifyPersonnelByPhoneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationMemberDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
