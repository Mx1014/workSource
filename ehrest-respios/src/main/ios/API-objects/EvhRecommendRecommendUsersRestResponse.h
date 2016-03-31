//
// EvhRecommendRecommendUsersRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhRecommendUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendRecommendUsersRestResponse
//
@interface EvhRecommendRecommendUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRecommendUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
