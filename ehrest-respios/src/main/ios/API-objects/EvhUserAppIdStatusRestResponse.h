//
// EvhUserAppIdStatusRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhAppIdStatusResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserAppIdStatusRestResponse
//
@interface EvhUserAppIdStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAppIdStatusResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
