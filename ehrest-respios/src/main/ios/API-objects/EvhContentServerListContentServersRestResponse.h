//
// EvhContentServerListContentServersRestResponse.h
// generated at 2016-04-07 14:16:31 
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
