//
// EvhTechparkRentalFindRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalFindRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalBillsRestResponse
//
@interface EvhTechparkRentalFindRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalFindRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
