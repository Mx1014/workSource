//
// EvhOrderBriefDTO.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrderBriefDTO
//
@interface EvhOrderBriefDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* period;

@property(nonatomic, copy) NSNumber* confType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

