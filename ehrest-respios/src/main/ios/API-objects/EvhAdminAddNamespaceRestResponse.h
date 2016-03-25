//
// EvhAdminAddNamespaceRestResponse.h
// generated at 2016-03-25 17:08:12 
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
