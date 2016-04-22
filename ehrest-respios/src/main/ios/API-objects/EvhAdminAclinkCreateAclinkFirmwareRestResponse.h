//
// EvhAdminAclinkCreateAclinkFirmwareRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhAclinkFirmwareDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateAclinkFirmwareRestResponse
//
@interface EvhAdminAclinkCreateAclinkFirmwareRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAclinkFirmwareDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
