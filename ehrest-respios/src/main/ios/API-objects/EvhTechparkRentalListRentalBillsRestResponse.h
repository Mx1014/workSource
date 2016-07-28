//
// EvhTechparkRentalListRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalListRentalBillsRestResponse
//
@interface EvhTechparkRentalListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
