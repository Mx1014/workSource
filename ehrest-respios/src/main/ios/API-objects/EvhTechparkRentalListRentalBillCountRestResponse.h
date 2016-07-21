//
// EvhTechparkRentalListRentalBillCountRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalListRentalBillCountCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalListRentalBillCountRestResponse
//
@interface EvhTechparkRentalListRentalBillCountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalListRentalBillCountCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
