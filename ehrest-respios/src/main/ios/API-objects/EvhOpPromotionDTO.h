//
// EvhOpPromotionDTO.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionDTO
//
@interface EvhOpPromotionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* sceneType;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* actionType;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* validCount;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

