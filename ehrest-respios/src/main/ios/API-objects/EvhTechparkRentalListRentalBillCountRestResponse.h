//
// EvhTechparkRentalListRentalBillCountRestResponse.h
// generated at 2016-04-07 17:57:44 
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
