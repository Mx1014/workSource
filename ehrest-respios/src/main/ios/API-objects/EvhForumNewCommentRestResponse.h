//
// EvhForumNewCommentRestResponse.h
// generated at 2016-04-12 15:02:21 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForumNewCommentRestResponse
//
@interface EvhForumNewCommentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
