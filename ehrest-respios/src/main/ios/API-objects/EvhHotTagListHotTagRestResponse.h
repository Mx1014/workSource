//
// EvhHotTagListHotTagRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhHotTagListHotTagRestResponse
//
@interface EvhHotTagListHotTagRestResponse : EvhRestResponseBase

// array of EvhTagDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
