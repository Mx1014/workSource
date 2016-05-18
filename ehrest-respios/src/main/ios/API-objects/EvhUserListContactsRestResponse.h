//
// EvhUserListContactsRestResponse.h
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
