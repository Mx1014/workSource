//
// EvhAdminAclinkSearchDoorAuthRestResponse.h
// generated at 2016-04-19 14:25:57 
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
