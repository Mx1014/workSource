//
// EvhAdminAclinkCreateDoorAccessGroupRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateDoorAccessGroupRestResponse
//
@interface EvhAdminAclinkCreateDoorAccessGroupRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
