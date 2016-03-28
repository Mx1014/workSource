//
// EvhUserListTopicFavoriteRestResponse.h
// generated at 2016-03-25 19:05:21 
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
