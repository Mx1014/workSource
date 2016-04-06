//
// EvhConfStartVideoConfRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhStartVideoConfResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfStartVideoConfRestResponse
//
@interface EvhConfStartVideoConfRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhStartVideoConfResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
