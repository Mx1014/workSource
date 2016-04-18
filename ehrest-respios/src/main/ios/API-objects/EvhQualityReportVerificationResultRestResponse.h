//
// EvhQualityReportVerificationResultRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhQualityInspectionTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityReportVerificationResultRestResponse
//
@interface EvhQualityReportVerificationResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQualityInspectionTaskDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
