//
// EvhUserGetUserInfoRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetUserInfoRestResponse
//
@interface EvhUserGetUserInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
