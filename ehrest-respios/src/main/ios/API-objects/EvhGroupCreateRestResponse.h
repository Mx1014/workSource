//
// EvhGroupCreateRestResponse.h
// generated at 2016-03-28 15:56:09 
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
