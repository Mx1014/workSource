//
// EvhHotTagSearchTagRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchTagResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhHotTagSearchTagRestResponse
//
@interface EvhHotTagSearchTagRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchTagResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
