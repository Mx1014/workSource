//
// EvhTechparkRentalAddRentalItemBillRestResponse.h
// generated at 2016-04-06 19:10:44 
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
