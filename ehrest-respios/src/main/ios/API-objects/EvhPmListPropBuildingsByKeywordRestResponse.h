//
// EvhPmListPropBuildingsByKeywordRestResponse.h
// generated at 2016-04-19 13:40:02 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPropBuildingsByKeywordRestResponse
//
@interface EvhPmListPropBuildingsByKeywordRestResponse : EvhRestResponseBase

// array of EvhAddressBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
