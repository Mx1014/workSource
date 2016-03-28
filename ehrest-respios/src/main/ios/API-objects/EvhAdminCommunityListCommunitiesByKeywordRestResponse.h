//
// EvhAdminCommunityListCommunitiesByKeywordRestResponse.h
// generated at 2016-03-25 19:05:21 
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
