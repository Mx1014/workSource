//
// EvhAdminForumSearchTopicsRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhSearchTopicAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminForumSearchTopicsRestResponse
//
@interface EvhAdminForumSearchTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchTopicAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
