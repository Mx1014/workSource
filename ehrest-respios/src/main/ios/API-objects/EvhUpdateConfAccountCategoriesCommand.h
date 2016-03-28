//
// EvhUpdateConfAccountCategoriesCommand.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateConfAccountCategoriesCommand
//
@interface EvhUpdateConfAccountCategoriesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* accountType;

@property(nonatomic, copy) NSString* confCapacity;

@property(nonatomic, copy) NSString* confType;

@property(nonatomic, copy) NSNumber* minimumMonths;

@property(nonatomic, copy) NSNumber* packagePrice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

