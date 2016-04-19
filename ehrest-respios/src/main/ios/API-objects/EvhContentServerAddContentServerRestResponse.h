//
// EvhContentServerAddContentServerRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhContentServerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContentServerAddContentServerRestResponse
//
@interface EvhContentServerAddContentServerRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhContentServerDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
