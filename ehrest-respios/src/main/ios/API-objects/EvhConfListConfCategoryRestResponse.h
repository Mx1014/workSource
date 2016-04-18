//
// EvhConfListConfCategoryRestResponse.h
// generated at 2016-04-18 14:48:52 
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
