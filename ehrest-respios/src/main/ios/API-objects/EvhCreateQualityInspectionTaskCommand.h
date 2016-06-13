//
// EvhCreateQualityInspectionTaskCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateQualityInspectionTaskCommand
//
@interface EvhCreateQualityInspectionTaskCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, strong) EvhStandardGroupDTO* group;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

