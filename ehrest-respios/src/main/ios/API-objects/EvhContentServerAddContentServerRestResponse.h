//
// EvhContentServerAddContentServerRestResponse.h
// generated at 2016-03-31 15:43:24 
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
