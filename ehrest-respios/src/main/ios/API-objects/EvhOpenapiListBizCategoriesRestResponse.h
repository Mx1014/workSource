//
// EvhOpenapiListBizCategoriesRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListBizCategoriesRestResponse
//
@interface EvhOpenapiListBizCategoriesRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
