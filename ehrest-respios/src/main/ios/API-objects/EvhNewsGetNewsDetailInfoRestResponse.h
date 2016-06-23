//
// EvhNewsGetNewsDetailInfoRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNewsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsGetNewsDetailInfoRestResponse
//
@interface EvhNewsGetNewsDetailInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNewsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
