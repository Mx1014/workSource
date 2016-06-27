//
// EvhAdminAclinkSearchVisitorDoorAuthRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListDoorAuthResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkSearchVisitorDoorAuthRestResponse
//
@interface EvhAdminAclinkSearchVisitorDoorAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAuthResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
