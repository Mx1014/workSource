//
// EvhForumCheckUserPostStatusRestResponse.h
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
