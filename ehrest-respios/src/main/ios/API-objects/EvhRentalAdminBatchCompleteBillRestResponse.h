//
// EvhRentalAdminBatchCompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2BatchCompleteBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminBatchCompleteBillRestResponse
//
@interface EvhRentalAdminBatchCompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2BatchCompleteBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
