//
// EvhTechparkRentalBatchIncompleteBillRestResponse.h
// generated at 2016-04-19 14:25:58 
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
