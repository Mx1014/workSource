//
// EvhGroupCreateRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupCreateRestResponse
//
@interface EvhGroupCreateRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGroupDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
