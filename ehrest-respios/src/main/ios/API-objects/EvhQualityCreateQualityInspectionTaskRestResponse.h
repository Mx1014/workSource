//
// EvhQualityCreateQualityInspectionTaskRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhQualityInspectionTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityCreateQualityInspectionTaskRestResponse
//
@interface EvhQualityCreateQualityInspectionTaskRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQualityInspectionTaskDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
