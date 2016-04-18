//
// EvhConfJoinVideoConfRestResponse.h
// generated at 2016-04-18 14:48:52 
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
