//
// EvhRentalAddRentalItemBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhAddRentalBillItemCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalItemBillRestResponse
//
@interface EvhRentalAddRentalItemBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAddRentalBillItemCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
