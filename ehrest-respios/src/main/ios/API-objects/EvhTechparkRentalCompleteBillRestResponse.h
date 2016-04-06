//
// EvhTechparkRentalCompleteBillRestResponse.h
// generated at 2016-04-06 19:10:44 
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
