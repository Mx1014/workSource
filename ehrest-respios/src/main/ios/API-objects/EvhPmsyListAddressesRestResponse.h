//
// EvhPmsyListAddressesRestResponse.h
// generated at 2016-04-29 18:56:04 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyListAddressesRestResponse
//
@interface EvhPmsyListAddressesRestResponse : EvhRestResponseBase

// array of EvhPmsyAddressDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
