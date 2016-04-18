//
// EvhAdminGroupSearchGroupRestResponse.h
// generated at 2016-04-18 14:48:52 
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
