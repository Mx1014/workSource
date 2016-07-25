//
// EvhTechparkRentalFindRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalBillsRestResponse
//
@interface EvhTechparkRentalFindRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
