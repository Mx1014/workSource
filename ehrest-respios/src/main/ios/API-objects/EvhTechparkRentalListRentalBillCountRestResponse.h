//
// EvhTechparkRentalListRentalBillCountRestResponse.h
// generated at 2016-04-06 19:59:47 
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
