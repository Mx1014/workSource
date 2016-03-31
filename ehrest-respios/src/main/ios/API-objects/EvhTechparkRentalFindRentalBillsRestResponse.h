//
// EvhTechparkRentalFindRentalBillsRestResponse.h
// generated at 2016-03-31 19:08:54 
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
