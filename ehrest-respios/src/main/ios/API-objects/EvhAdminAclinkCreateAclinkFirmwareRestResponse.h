//
// EvhAdminAclinkCreateAclinkFirmwareRestResponse.h
// generated at 2016-04-19 13:40:01 
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
