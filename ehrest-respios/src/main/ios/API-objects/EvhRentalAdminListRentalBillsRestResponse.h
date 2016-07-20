//
// EvhRentalAdminListRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2ListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminListRentalBillsRestResponse
//
@interface EvhRentalAdminListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2ListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
