//
// EvhUserListTopicFavoriteRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListTopicFavoriteRestResponse
//
@interface EvhUserListTopicFavoriteRestResponse : EvhRestResponseBase

// array of EvhPostDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
