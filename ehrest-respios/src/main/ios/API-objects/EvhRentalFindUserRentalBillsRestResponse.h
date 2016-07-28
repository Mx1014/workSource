//
// EvhRentalFindUserRentalBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2FindRentalBillsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindUserRentalBillsRestResponse
//
@interface EvhRentalFindUserRentalBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2FindRentalBillsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
