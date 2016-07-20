//
// EvhTechparkRentalBatchIncompleteBillRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhBatchCompleteBillCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalBatchIncompleteBillRestResponse
//
@interface EvhTechparkRentalBatchIncompleteBillRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBatchCompleteBillCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
