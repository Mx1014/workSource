//
// EvhVideoConfAccountRuleDTO.h
// generated at 2016-03-31 20:15:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountRuleDTO
//
@interface EvhVideoConfAccountRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* multipleAccountThreshold;

@property(nonatomic, copy) NSString* confCapacity;

@property(nonatomic, copy) NSString* confType;

@property(nonatomic, copy) NSNumber* minPeriod;

@property(nonatomic, copy) NSNumber* singleAccountPrice;

@property(nonatomic, copy) NSNumber* multipleAccountPrice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

