//
// EvhConfStartVideoConfRestResponse.h
// generated at 2016-03-31 10:18:21 
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
