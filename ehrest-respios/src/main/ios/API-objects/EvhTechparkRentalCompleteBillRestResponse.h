//
// EvhTechparkRentalCompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalCompleteBillRestResponse
//
@interface EvhTechparkRentalCompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
