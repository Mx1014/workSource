//
// EvhCommunityListCommunitiesByNamespaceIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunitiesByKeywordCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityListCommunitiesByNamespaceIdRestResponse
//
@interface EvhCommunityListCommunitiesByNamespaceIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunitiesByKeywordCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
