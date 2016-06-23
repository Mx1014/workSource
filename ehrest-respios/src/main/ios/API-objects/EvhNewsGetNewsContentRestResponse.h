//
// EvhNewsGetNewsContentRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNewsContentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsGetNewsContentRestResponse
//
@interface EvhNewsGetNewsContentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNewsContentDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
