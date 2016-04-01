//
// EvhUserListContactsRestResponse.h
// generated at 2016-04-01 15:40:24 
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
