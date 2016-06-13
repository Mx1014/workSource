//
// EvhQualityListQualityInspectionTasksRestResponse.h
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
