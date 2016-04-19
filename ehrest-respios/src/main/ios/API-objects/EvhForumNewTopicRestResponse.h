//
// EvhForumNewTopicRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForumNewTopicRestResponse
//
@interface EvhForumNewTopicRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
