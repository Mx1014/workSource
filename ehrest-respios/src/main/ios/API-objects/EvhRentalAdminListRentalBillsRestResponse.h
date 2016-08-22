//
// EvhRentalAdminListRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminListRentalBillsRestResponse
//
@interface EvhRentalAdminListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
