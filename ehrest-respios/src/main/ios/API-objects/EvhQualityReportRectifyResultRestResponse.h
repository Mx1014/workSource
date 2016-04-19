//
// EvhQualityReportRectifyResultRestResponse.h
// generated at 2016-04-19 12:41:55 
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
