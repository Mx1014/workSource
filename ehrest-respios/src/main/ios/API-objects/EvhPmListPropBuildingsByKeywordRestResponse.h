//
// EvhPmListPropBuildingsByKeywordRestResponse.h
// generated at 2016-03-28 15:56:09 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPropBuildingsByKeywordRestResponse
//
@interface EvhPmListPropBuildingsByKeywordRestResponse : EvhRestResponseBase

// array of EvhBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
