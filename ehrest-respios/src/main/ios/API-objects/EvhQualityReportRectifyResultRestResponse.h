//
// EvhQualityReportRectifyResultRestResponse.h
// generated at 2016-04-22 13:56:51 
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
