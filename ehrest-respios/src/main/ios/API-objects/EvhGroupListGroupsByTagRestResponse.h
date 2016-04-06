//
// EvhGroupListGroupsByTagRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListGroupCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListGroupsByTagRestResponse
//
@interface EvhGroupListGroupsByTagRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListGroupCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
