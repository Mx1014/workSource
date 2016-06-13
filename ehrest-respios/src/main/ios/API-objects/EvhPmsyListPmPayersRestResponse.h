//
// EvhPmsyListPmPayersRestResponse.h
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
