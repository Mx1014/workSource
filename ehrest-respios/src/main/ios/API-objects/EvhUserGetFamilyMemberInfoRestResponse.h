//
// EvhUserGetFamilyMemberInfoRestResponse.h
// generated at 2016-04-18 14:48:53 
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
