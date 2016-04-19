//
// EvhQualityListQualityInspectionTasksRestResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:58 
>>>>>>> 3.3.x
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
