//
// EvhTechparkRentalAddRentalItemBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalAddRentalBillItemCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalAddRentalItemBillRestResponse
//
@interface EvhTechparkRentalAddRentalItemBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalAddRentalBillItemCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
