//
// EvhConfJoinVideoConfRestResponse.h
// generated at 2016-03-25 17:08:12 
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
