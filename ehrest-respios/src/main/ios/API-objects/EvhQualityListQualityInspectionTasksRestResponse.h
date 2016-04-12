//
// EvhQualityListQualityInspectionTasksRestResponse.h
// generated at 2016-04-12 15:02:21 
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
