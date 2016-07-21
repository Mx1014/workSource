//
// EvhTechparkRentalVerifyServiceRentalBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalVerifyRentalBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalVerifyServiceRentalBillRestResponse
//
@interface EvhTechparkRentalVerifyServiceRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalVerifyRentalBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
