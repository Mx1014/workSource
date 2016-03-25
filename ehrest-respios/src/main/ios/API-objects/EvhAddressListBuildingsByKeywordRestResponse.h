//
// EvhAddressListBuildingsByKeywordRestResponse.h
// generated at 2016-03-25 17:08:12 
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
