//
// EvhGroupGetRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupGetRestResponse
//
@interface EvhGroupGetRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGroupDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
