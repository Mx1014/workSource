//
// EvhUserListTopicFavoriteRestResponse.h
// generated at 2016-04-08 20:09:24 
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
