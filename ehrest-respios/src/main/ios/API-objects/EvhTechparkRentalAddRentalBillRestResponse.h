//
// EvhTechparkRentalAddRentalBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalAddRentalBillRestResponse
//
@interface EvhTechparkRentalAddRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
