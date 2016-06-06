//
// EvhAclinkGetVisitorRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetVisitorResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkGetVisitorRestResponse
//
@interface EvhAclinkGetVisitorRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetVisitorResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
