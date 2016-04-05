//
// EvhAclinkQueryMessagesRestResponse.h
// generated at 2016-04-05 13:45:26 
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
