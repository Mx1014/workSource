//
// EvhAdminGroupSearchGroupRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhListGroupCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminGroupSearchGroupRestResponse
//
@interface EvhAdminGroupSearchGroupRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListGroupCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
