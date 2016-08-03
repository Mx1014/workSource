//
// EvhRentalAdminGetRentalBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetRentalBillRestResponse
//
@interface EvhRentalAdminGetRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2RentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
