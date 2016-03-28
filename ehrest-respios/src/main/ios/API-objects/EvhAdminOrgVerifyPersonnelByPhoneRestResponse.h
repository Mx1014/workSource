//
// EvhAdminOrgVerifyPersonnelByPhoneRestResponse.h
// generated at 2016-03-25 19:05:21 
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
