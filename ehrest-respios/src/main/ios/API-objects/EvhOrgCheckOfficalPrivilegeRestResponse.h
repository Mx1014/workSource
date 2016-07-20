//
// EvhOrgCheckOfficalPrivilegeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCheckOfficalPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgCheckOfficalPrivilegeRestResponse
//
@interface EvhOrgCheckOfficalPrivilegeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCheckOfficalPrivilegeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
