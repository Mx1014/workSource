//
// EvhUserListContactsRestResponse.h
// generated at 2016-03-30 10:13:10 
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
