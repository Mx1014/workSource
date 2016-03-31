//
// EvhForumListTopicsRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhListPostCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForumListTopicsRestResponse
//
@interface EvhForumListTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
