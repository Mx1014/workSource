//
// EvhAdminAclinkSearchDoorAccessRestResponse.h
// generated at 2016-04-18 14:48:52 
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
