//
// EvhNewsAddNewsCommentRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNewsCommentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsAddNewsCommentRestResponse
//
@interface EvhNewsAddNewsCommentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNewsCommentDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
