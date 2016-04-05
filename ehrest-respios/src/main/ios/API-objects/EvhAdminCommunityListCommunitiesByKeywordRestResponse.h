//
// EvhAdminCommunityListCommunitiesByKeywordRestResponse.h
// generated at 2016-04-05 13:45:27 
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
