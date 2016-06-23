//
// EvhNewsAddNewsCommentRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhAddNewsCommentResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsAddNewsCommentRestResponse
//
@interface EvhNewsAddNewsCommentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAddNewsCommentResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
