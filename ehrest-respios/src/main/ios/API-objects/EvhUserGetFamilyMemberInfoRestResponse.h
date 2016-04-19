//
// EvhUserGetFamilyMemberInfoRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetFamilyMemberInfoRestResponse
//
@interface EvhUserGetFamilyMemberInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
