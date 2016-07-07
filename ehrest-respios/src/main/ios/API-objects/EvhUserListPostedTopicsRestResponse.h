//
// EvhUserListPostedTopicsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListPostResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListPostedTopicsRestResponse
//
@interface EvhUserListPostedTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
