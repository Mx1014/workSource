//
// EvhVideoConfAccountRuleDTO.h
// generated at 2016-03-25 15:57:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountRuleDTO
//
@interface EvhVideoConfAccountRuleDTO
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

