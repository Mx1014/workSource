//
// EvhRepeatTestExpressionAnalyzeRestResponse.h
// generated at 2016-04-22 13:56:51 
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
