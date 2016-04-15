//
// EvhTechparkOnlinePayOnlinePayBillRestResponse.h
// generated at 2016-04-12 15:02:21 
//
#import "RestResponseBase.h"
#import "EvhRechargeInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkOnlinePayOnlinePayBillRestResponse
//
@interface EvhTechparkOnlinePayOnlinePayBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeInfoDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
