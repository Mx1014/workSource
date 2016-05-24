//
// EvhPmsyListAddressesRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyListAddressesRestResponse
//
@interface EvhPmsyListAddressesRestResponse : EvhRestResponseBase

// array of EvhAddressDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
