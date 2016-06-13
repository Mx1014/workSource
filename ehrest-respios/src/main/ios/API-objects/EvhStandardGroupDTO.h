//
// EvhStandardGroupDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStandardGroupDTO
//
@interface EvhStandardGroupDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* groupType;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSNumber* standardId;

@property(nonatomic, copy) NSNumber* inspectorUid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

