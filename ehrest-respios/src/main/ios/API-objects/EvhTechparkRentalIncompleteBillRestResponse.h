//
// EvhTechparkRentalIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalIncompleteBillRestResponse
//
@interface EvhTechparkRentalIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
