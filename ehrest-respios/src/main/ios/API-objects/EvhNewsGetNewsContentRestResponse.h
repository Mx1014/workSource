//
// EvhNewsGetNewsContentRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetNewsContentResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsGetNewsContentRestResponse
//
@interface EvhNewsGetNewsContentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetNewsContentResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
