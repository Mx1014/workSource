//
// EvhConfListConfCategoryRestResponse.h
// generated at 2016-04-07 10:47:33 
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
