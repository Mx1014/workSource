//
// EvhUiUserGetUserRelatedAddressesRestResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:53 
=======
// generated at 2016-04-19 14:25:58 
>>>>>>> 3.3.x
//
#import "RestResponseBase.h"
#import "EvhGetUserRelatedAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserGetUserRelatedAddressesRestResponse
//
@interface EvhUiUserGetUserRelatedAddressesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetUserRelatedAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
