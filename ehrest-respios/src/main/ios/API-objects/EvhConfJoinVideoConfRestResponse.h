//
// EvhConfJoinVideoConfRestResponse.h
// generated at 2016-03-31 15:43:24 
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
