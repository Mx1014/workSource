//
// EvhQualityListRecordsByTaskIdRestResponse.h
// generated at 2016-04-19 12:41:55 
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
