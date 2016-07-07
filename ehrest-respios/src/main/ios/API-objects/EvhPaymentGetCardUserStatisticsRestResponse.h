//
// EvhPaymentGetCardUserStatisticsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetCardUserStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentGetCardUserStatisticsRestResponse
//
@interface EvhPaymentGetCardUserStatisticsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetCardUserStatisticsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
