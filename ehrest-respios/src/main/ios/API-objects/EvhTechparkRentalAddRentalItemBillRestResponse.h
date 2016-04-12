//
// EvhTechparkRentalAddRentalItemBillRestResponse.h
// generated at 2016-04-08 20:09:24 
//
#import "RestResponseBase.h"
#import "EvhAddRentalBillItemCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalAddRentalItemBillRestResponse
//
@interface EvhTechparkRentalAddRentalItemBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAddRentalBillItemCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
