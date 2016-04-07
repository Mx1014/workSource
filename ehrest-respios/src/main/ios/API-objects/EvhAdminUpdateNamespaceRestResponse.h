//
// EvhAdminUpdateNamespaceRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhNamespaceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUpdateNamespaceRestResponse
//
@interface EvhAdminUpdateNamespaceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNamespaceDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
