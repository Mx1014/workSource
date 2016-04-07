//
// EvhActivityListActivityCategoriesRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"
#import "EvhListActivityCategories.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListActivityCategoriesRestResponse
//
@interface EvhActivityListActivityCategoriesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivityCategories* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
