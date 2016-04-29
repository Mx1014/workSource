//
// EvhQualityReportVerificationResultRestResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:58 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:57 
>>>>>>> 3.3.x
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
