//
// EvhTechparkRentalBatchIncompleteBillRestResponse.h
// generated at 2016-04-06 19:59:47 
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
