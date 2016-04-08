//
// EvhUserAppIdStatusRestResponse.h
// generated at 2016-04-07 17:57:44 
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
