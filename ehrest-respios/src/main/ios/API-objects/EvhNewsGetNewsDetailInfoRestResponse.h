//
// EvhNewsGetNewsDetailInfoRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetNewsDetailInfoResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsGetNewsDetailInfoRestResponse
//
@interface EvhNewsGetNewsDetailInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetNewsDetailInfoResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
