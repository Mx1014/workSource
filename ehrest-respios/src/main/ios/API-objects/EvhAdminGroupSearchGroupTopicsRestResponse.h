//
// EvhAdminGroupSearchGroupTopicsRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhSearchTopicAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminGroupSearchGroupTopicsRestResponse
//
@interface EvhAdminGroupSearchGroupTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchTopicAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
