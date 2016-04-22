//
// EvhTechparkRentalVerifyServiceRentalBillRestResponse.h
// generated at 2016-04-22 13:56:51 
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
