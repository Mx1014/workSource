//
// EvhTechparkRentalListRentalBillsRestResponse.h
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
