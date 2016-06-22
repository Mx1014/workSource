//
// EvhAdminAclinkDeleteDoorAccessByIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkDeleteDoorAccessByIdRestResponse
//
@interface EvhAdminAclinkDeleteDoorAccessByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
