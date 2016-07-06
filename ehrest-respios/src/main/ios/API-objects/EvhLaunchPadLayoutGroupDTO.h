//
// EvhLaunchPadLayoutGroupDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutGroupDTO
//
@interface EvhLaunchPadLayoutGroupDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* widget;

@property(nonatomic, copy) NSString* instanceConfig;

@property(nonatomic, copy) NSString* style;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* separatorFlag;

@property(nonatomic, copy) NSNumber* separatorHeight;

@property(nonatomic, copy) NSNumber* columnCount;

@property(nonatomic, copy) NSNumber* editFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

