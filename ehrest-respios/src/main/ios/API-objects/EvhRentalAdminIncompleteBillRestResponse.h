//
// EvhRentalAdminIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminIncompleteBillRestResponse
//
@interface EvhRentalAdminIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2RentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
