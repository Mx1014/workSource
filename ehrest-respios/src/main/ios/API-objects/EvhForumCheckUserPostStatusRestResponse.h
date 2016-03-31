//
// EvhForumCheckUserPostStatusRestResponse.h
// generated at 2016-03-31 13:49:15 
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
