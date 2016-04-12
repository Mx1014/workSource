//
// EvhTechparkRentalVerifyServiceRentalBillRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhVerifyRentalBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalVerifyServiceRentalBillRestResponse
//
@interface EvhTechparkRentalVerifyServiceRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVerifyRentalBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
