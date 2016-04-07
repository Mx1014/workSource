//
// EvhAddressListBuildingsByKeywordRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListBuildingsByKeywordRestResponse
//
@interface EvhAddressListBuildingsByKeywordRestResponse : EvhRestResponseBase

// array of EvhAddressBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
