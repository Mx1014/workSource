//
// EvhRentalListRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalListRentalBillsRestResponse
//
@interface EvhRentalListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
