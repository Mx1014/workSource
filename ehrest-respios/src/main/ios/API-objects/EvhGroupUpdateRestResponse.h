//
// EvhGroupUpdateRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupUpdateRestResponse
//
@interface EvhGroupUpdateRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGroupDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
