//
// EvhAdminAddNamespaceRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNamespaceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAddNamespaceRestResponse
//
@interface EvhAdminAddNamespaceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNamespaceDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
