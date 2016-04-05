//
// EvhAdminAclinkSearchDoorAccessRestResponse.h
// generated at 2016-04-05 13:45:26 
//
#import "RestResponseBase.h"
#import "EvhListDoorAccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkSearchDoorAccessRestResponse
//
@interface EvhAdminAclinkSearchDoorAccessRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
