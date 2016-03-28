//
// EvhAdminForumSearchCommentsRestResponse.h
// generated at 2016-03-25 19:05:21 
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
