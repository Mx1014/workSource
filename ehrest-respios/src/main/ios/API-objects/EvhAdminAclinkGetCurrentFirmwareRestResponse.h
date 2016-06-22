//
// EvhAdminAclinkGetCurrentFirmwareRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListDoorAccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkGetCurrentFirmwareRestResponse
//
@interface EvhAdminAclinkGetCurrentFirmwareRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
