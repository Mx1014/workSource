//
// EvhListActivityCategories.h
// generated at 2016-04-19 13:40:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActivityCategories
//
@interface EvhListActivityCategories
    : NSObject<EvhJsonSerializable>


// item type EvhCategoryDTO*
@property(nonatomic, strong) NSMutableArray* activityCategories;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

