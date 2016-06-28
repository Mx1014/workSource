//
// EvhPaymentSearchCardUsersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchCardUsersResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentSearchCardUsersRestResponse
//
@interface EvhPaymentSearchCardUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchCardUsersResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
