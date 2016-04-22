//
// EvhAclinkActivingRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkActivingRestResponse
//
@interface EvhAclinkActivingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorMessage* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
