//
// EvhRepeatTestExpressionAnalyzeRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRepeatTestExpressionAnalyzeRestResponse
//
@interface EvhRepeatTestExpressionAnalyzeRestResponse : EvhRestResponseBase

// array of EvhRepeatExpressionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
