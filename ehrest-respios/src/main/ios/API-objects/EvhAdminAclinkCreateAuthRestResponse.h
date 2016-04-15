//
// EvhAdminAclinkCreateAuthRestResponse.h
// generated at 2016-04-12 15:02:20 
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
