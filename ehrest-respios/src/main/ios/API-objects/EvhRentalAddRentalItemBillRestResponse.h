//
// EvhRentalAddRentalItemBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2AddRentalBillItemCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalItemBillRestResponse
//
@interface EvhRentalAddRentalItemBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2AddRentalBillItemCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
