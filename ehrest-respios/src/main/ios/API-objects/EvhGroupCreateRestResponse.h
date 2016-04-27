//
// EvhGroupCreateRestResponse.h
// generated at 2016-04-26 18:22:56 
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
