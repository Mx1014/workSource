//
// EvhUserListContactsRestResponse.h
// generated at 2016-04-08 20:09:24 
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
