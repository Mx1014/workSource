//
// EvhAclinkActiveRestResponse.h
// generated at 2016-03-31 20:15:33 
//
#import "RestResponseBase.h"
#import "EvhQueryDoorMessageResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkActiveRestResponse
//
@interface EvhAclinkActiveRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQueryDoorMessageResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
