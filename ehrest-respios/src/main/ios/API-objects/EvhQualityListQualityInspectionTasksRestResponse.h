//
// EvhQualityListQualityInspectionTasksRestResponse.h
// generated at 2016-04-19 13:40:02 
//
#import "RestResponseBase.h"
#import "EvhListQualityInspectionTasksResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListQualityInspectionTasksRestResponse
//
@interface EvhQualityListQualityInspectionTasksRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListQualityInspectionTasksResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
