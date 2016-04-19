//
// EvhCategoryListInterestCategoriesRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListInterestCategoriesRestResponse
//
@interface EvhCategoryListInterestCategoriesRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
