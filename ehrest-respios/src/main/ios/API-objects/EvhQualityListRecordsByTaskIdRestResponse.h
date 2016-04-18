//
// EvhQualityListRecordsByTaskIdRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListRecordsByTaskIdRestResponse
//
@interface EvhQualityListRecordsByTaskIdRestResponse : EvhRestResponseBase

// array of EvhQualityInspectionTaskRecordsDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
