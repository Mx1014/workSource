//
// EvhAdminAclinkCreateAuthRestResponse.h
// generated at 2016-04-19 12:41:54 
//
#import "RestResponseBase.h"
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkCreateAuthRestResponse
//
@interface EvhAdminAclinkCreateAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAuthDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
