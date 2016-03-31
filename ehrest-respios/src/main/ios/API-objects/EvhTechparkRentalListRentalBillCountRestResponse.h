//
// EvhTechparkRentalListRentalBillCountRestResponse.h
// generated at 2016-03-31 13:49:15 
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
