//
// EvhAdminAclinkListDoorAccessGroupRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListDoorAccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkListDoorAccessGroupRestResponse
//
@interface EvhAdminAclinkListDoorAccessGroupRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
