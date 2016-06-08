//
// EvhUserListPostedActivitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListPostResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListPostedActivitiesRestResponse
//
@interface EvhUserListPostedActivitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
