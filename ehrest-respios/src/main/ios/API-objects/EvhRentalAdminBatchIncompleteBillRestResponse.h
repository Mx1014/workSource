//
// EvhRentalAdminBatchIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhBatchCompleteBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminBatchIncompleteBillRestResponse
//
@interface EvhRentalAdminBatchIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBatchCompleteBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
