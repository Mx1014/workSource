//
// EvhConfListConfCategoryRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListConfCategoryResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListConfCategoryRestResponse
//
@interface EvhConfListConfCategoryRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListConfCategoryResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
