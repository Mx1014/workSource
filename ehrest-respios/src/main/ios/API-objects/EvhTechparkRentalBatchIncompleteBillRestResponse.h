//
// EvhTechparkRentalBatchIncompleteBillRestResponse.h
// generated at 2016-04-18 14:48:52 
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
