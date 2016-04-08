//
// EvhTechparkOnlinePayOnlinePayBillRestResponse.h
// generated at 2016-04-08 20:09:24 
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
