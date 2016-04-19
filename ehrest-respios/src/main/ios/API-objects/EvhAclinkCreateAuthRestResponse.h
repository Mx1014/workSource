//
// EvhAclinkCreateAuthRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkCreateAuthRestResponse
//
@interface EvhAclinkCreateAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAuthDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
