//
// EvhRentalListRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2ListRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalListRentalBillsRestResponse
//
@interface EvhRentalListRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2ListRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
