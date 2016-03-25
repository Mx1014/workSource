//
// EvhUserAppIdStatusRestResponse.h
// generated at 2016-03-25 17:08:13 
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
