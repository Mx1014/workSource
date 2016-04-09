//
// EvhAdminOrgVerifyPersonnelByPhoneRestResponse.h
// generated at 2016-04-08 20:09:23 
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
