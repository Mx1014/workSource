//
// EvhQualityReportRectifyResultRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhQualityInspectionTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityReportRectifyResultRestResponse
//
@interface EvhQualityReportRectifyResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQualityInspectionTaskDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
