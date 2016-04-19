//
// EvhAdminGroupSearchGroupTopicsRestResponse.h
// generated at 2016-04-19 12:41:54 
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
