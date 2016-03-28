//
// EvhForumCheckUserPostStatusRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhCheckUserPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForumCheckUserPostStatusRestResponse
//
@interface EvhForumCheckUserPostStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCheckUserPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
