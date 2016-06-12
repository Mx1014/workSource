//
// EvhHotTagSearchTagRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhHotTagSearchTagRestResponse
//
@interface EvhHotTagSearchTagRestResponse : EvhRestResponseBase

// array of EvhTagDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
