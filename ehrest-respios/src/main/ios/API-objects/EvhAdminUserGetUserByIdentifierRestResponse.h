//
// EvhAdminUserGetUserByIdentifierRestResponse.h
// generated at 2016-03-28 15:56:09 
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
