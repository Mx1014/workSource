//
// EvhTechparkRentalBatchIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalBatchCompleteBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalBatchIncompleteBillRestResponse
//
@interface EvhTechparkRentalBatchIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalBatchCompleteBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
