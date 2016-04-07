//
// EvhAdminAclListWebMenuRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"
#import "EvhListWebMenuResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListWebMenuRestResponse
//
@interface EvhAdminAclListWebMenuRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListWebMenuResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
