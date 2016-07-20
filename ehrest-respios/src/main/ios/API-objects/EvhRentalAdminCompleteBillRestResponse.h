//
// EvhRentalAdminCompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminCompleteBillRestResponse
//
@interface EvhRentalAdminCompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2RentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
