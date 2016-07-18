//
// EvhNewsListNewsCommentRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNewsCommentResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsListNewsCommentRestResponse
//
@interface EvhNewsListNewsCommentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNewsCommentResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
