//
// EvhTechparkRentalListRentalBillsRestResponse.h
// generated at 2016-04-06 19:10:44 
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
