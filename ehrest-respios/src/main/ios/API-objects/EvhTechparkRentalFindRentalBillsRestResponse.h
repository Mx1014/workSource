//
// EvhTechparkRentalFindRentalBillsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
