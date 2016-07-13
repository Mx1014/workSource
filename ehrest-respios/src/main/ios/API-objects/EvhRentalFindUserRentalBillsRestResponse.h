//
// EvhRentalFindUserRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindUserRentalBillsRestResponse
//
@interface EvhRentalFindUserRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
