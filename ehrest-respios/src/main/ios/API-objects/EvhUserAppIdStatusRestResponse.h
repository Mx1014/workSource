//
// EvhUserAppIdStatusRestResponse.h
// generated at 2016-04-06 19:59:47 
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
