//
// EvhTechparkRentalListRentalBillCountRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListRentalBillCountCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalListRentalBillCountRestResponse
//
@interface EvhTechparkRentalListRentalBillCountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRentalBillCountCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
