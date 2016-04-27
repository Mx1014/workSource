//
// EvhTechparkOnlinePayOnlinePayBillRestResponse.h
// generated at 2016-04-26 18:22:57 
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
