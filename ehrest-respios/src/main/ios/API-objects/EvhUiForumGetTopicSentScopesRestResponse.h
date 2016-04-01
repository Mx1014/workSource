//
// EvhUiForumGetTopicSentScopesRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiForumGetTopicSentScopesRestResponse
//
@interface EvhUiForumGetTopicSentScopesRestResponse : EvhRestResponseBase

// array of EvhTopicScopeDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
