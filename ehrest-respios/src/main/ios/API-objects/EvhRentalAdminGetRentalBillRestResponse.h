//
// EvhRentalAdminGetRentalBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetRentalBillRestResponse
//
@interface EvhRentalAdminGetRentalBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalBillDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
