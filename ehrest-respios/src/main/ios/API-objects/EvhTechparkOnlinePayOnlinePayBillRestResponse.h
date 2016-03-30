//
// EvhTechparkOnlinePayOnlinePayBillRestResponse.h
// generated at 2016-03-30 10:13:09 
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
