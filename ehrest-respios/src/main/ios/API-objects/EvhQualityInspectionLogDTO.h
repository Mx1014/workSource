//
// EvhQualityInspectionLogDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionLogDTO
//
@interface EvhQualityInspectionLogDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* operatorId;

@property(nonatomic, copy) NSString* operatorName;

@property(nonatomic, copy) NSNumber* operateType;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* targetName;

@property(nonatomic, copy) NSNumber* operateTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

