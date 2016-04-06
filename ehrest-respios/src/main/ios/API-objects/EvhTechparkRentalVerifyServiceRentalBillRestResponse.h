//
// EvhTechparkRentalVerifyServiceRentalBillRestResponse.h
// generated at 2016-04-06 19:10:44 
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
