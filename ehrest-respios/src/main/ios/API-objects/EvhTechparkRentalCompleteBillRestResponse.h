//
// EvhTechparkRentalCompleteBillRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalCompleteBillRestResponse
//
@interface EvhTechparkRentalCompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
