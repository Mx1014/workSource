//
// EvhForumListTopicsRestResponse.h
// generated at 2016-04-08 20:09:23 
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
