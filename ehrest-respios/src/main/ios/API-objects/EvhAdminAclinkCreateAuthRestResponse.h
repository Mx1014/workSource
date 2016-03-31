//
// EvhAdminAclinkCreateAuthRestResponse.h
// generated at 2016-03-31 15:43:23 
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
