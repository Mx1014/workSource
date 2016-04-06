//
// EvhConfJoinVideoConfRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhJoinVideoConfResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfJoinVideoConfRestResponse
//
@interface EvhConfJoinVideoConfRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhJoinVideoConfResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
