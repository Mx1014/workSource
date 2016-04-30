//
// EvhPmsyListAddressesRestResponse.h
// generated at 2016-04-30 11:16:58 
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
