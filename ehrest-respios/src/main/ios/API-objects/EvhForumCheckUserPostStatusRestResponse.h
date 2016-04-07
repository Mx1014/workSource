//
// EvhForumCheckUserPostStatusRestResponse.h
// generated at 2016-04-07 14:16:31 
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
