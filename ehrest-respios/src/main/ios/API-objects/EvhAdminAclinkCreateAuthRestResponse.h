//
// EvhAdminAclinkCreateAuthRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateAuthRestResponse
//
@interface EvhAdminAclinkCreateAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAuthDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
