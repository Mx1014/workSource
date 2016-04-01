//
// EvhAclinkActivingRestResponse.h
// generated at 2016-04-01 15:40:23 
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
