//
// EvhCategoryListAllCategoriesRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListAllCategoriesRestResponse
//
@interface EvhCategoryListAllCategoriesRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
