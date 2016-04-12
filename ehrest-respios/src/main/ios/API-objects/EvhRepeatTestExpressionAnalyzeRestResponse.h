//
// EvhRepeatTestExpressionAnalyzeRestResponse.h
// generated at 2016-04-12 15:02:21 
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
