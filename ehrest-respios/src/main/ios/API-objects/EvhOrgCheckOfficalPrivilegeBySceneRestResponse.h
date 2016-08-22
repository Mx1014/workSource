//
// EvhOrgCheckOfficalPrivilegeBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCheckOfficalPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgCheckOfficalPrivilegeBySceneRestResponse
//
@interface EvhOrgCheckOfficalPrivilegeBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCheckOfficalPrivilegeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
