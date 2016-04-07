//
// EvhTechparkRentalAddRentalItemBillRestResponse.h
// generated at 2016-04-07 10:47:33 
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
