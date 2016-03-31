//
// EvhAddressListBuildingsByKeywordRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListBuildingsByKeywordRestResponse
//
@interface EvhAddressListBuildingsByKeywordRestResponse : EvhRestResponseBase

// array of EvhBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
