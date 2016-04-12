//
// EvhTechparkRentalFindRentalBillsRestResponse.h
// generated at 2016-04-08 20:09:24 
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
