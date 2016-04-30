//
// EvhPmsyListPmPayersRestResponse.h
// generated at 2016-04-30 11:16:58 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyListPmPayersRestResponse
//
@interface EvhPmsyListPmPayersRestResponse : EvhRestResponseBase

// array of EvhPmsyPayerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
