//
// EvhAdminCommunityListCommunitiesByKeywordRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListCommunitiesByKeywordCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListCommunitiesByKeywordRestResponse
//
@interface EvhAdminCommunityListCommunitiesByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunitiesByKeywordCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
