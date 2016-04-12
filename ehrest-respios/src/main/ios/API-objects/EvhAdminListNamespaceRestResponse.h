//
// EvhAdminListNamespaceRestResponse.h
// generated at 2016-04-12 15:02:20 
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
