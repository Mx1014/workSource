//
// EvhAdminAclinkSearchDoorAuthRestResponse.h
// generated at 2016-04-07 17:33:49 
//
#import "RestResponseBase.h"
#import "EvhListDoorAuthResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkSearchDoorAuthRestResponse
//
@interface EvhAdminAclinkSearchDoorAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAuthResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
