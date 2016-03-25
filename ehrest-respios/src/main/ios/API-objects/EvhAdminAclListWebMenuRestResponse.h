//
// EvhAdminAclListWebMenuRestResponse.h
// generated at 2016-03-25 17:08:12 
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
