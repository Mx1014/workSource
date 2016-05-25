//
// EvhAdminAclinkCreateDoorAccessLingLingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateDoorAccessLingLingRestResponse
//
@interface EvhAdminAclinkCreateDoorAccessLingLingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
