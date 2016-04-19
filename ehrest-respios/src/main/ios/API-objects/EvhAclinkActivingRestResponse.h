//
// EvhAclinkActivingRestResponse.h
// generated at 2016-04-19 13:40:01 
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
