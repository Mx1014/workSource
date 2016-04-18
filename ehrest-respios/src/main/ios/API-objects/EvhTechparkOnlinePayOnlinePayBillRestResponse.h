//
// EvhTechparkOnlinePayOnlinePayBillRestResponse.h
// generated at 2016-04-18 14:48:52 
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
