//
// EvhAdminListNamespaceRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminListNamespaceRestResponse
//
@interface EvhAdminListNamespaceRestResponse : EvhRestResponseBase

// array of EvhNamespaceDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
