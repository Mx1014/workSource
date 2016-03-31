//
// EvhContentServerListContentServersRestResponse.h
// generated at 2016-03-31 19:08:54 
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
