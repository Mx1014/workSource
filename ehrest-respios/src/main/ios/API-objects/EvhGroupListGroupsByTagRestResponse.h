//
// EvhGroupListGroupsByTagRestResponse.h
// generated at 2016-03-25 17:08:13 
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
