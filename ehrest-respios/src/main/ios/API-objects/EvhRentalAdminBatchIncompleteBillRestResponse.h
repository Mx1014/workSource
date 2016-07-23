//
// EvhRentalAdminBatchIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2BatchCompleteBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminBatchIncompleteBillRestResponse
//
@interface EvhRentalAdminBatchIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2BatchCompleteBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
