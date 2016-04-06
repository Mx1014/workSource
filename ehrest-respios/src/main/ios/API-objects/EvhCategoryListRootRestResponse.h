//
// EvhCategoryListRootRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListRootRestResponse
//
@interface EvhCategoryListRootRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
