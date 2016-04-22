//
// EvhAclinkQueryMessagesRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhQueryDoorMessageResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkQueryMessagesRestResponse
//
@interface EvhAclinkQueryMessagesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQueryDoorMessageResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
