//
// EvhConfGetSourceVideoConfAccountStatisticsRestResponse.h
// generated at 2016-03-30 10:13:09 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfGetSourceVideoConfAccountStatisticsRestResponse
//
@interface EvhConfGetSourceVideoConfAccountStatisticsRestResponse : EvhRestResponseBase

// array of EvhSourceVideoConfAccountStatistics* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
