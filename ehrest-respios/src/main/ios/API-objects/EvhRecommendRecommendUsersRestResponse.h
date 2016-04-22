//
// EvhRecommendRecommendUsersRestResponse.h
// generated at 2016-04-22 13:56:51 
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
