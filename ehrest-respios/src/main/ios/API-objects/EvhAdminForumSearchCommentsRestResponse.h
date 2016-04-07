//
// EvhAdminForumSearchCommentsRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhSearchTopicAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminForumSearchCommentsRestResponse
//
@interface EvhAdminForumSearchCommentsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchTopicAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
