//
// EvhTechparkRentalListRentalBillsRestResponse.h
// generated at 2016-03-31 15:43:24 
//
#import "RestResponseBase.h"
#import "EvhListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalListRentalBillsRestResponse
//
@interface EvhTechparkRentalListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
