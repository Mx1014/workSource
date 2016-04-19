//
// EvhAdminUserGetUserByIdentifierRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserGetUserByIdentifierRestResponse
//
@interface EvhAdminUserGetUserByIdentifierRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
