//
// EvhUserListPostedActivitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListPostedActivitiesRestResponse
//
@interface EvhUserListPostedActivitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
