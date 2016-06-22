//
// EvhAdminAclinkCreateLingingVistorRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateLingingVistorRestResponse
//
@interface EvhAdminAclinkCreateLingingVistorRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAuthDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
