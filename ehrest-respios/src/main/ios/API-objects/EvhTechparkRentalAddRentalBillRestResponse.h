//
// EvhTechparkRentalAddRentalBillRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalAddRentalBillRestResponse
//
@interface EvhTechparkRentalAddRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
