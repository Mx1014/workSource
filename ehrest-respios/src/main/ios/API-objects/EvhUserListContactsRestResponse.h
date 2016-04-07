//
// EvhUserListContactsRestResponse.h
// generated at 2016-04-07 14:16:32 
//
#import "RestResponseBase.h"
#import "EvhListContactRespose.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListContactsRestResponse
//
@interface EvhUserListContactsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListContactRespose* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
