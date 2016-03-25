//
// EvhTechparkRentalAddRentalItemBillRestResponse.h
// generated at 2016-03-25 09:26:45 
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
