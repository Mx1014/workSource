//
// EvhAdminOrgVerifyPersonnelByPhoneRestResponse.h
// generated at 2016-03-28 15:56:09 
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
