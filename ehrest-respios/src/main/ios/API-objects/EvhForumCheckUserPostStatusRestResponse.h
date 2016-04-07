//
// EvhForumCheckUserPostStatusRestResponse.h
// generated at 2016-04-07 10:47:33 
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
