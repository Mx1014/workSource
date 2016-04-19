//
// EvhAdminAclinkCreateAclinkFirmwareRestResponse.h
// generated at 2016-04-19 12:41:54 
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
