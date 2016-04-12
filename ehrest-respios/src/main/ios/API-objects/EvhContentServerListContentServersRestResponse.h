//
// EvhContentServerListContentServersRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContentServerListContentServersRestResponse
//
@interface EvhContentServerListContentServersRestResponse : EvhRestResponseBase

// array of EvhContentServerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
