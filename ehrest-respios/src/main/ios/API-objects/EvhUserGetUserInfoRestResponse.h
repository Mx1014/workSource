//
// EvhUserGetUserInfoRestResponse.h
// generated at 2016-03-28 15:56:09 
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
