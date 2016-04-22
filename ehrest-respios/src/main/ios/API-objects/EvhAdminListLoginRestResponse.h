//
// EvhAdminListLoginRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminListLoginRestResponse
//
@interface EvhAdminListLoginRestResponse : EvhRestResponseBase

// array of EvhUserLoginDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
