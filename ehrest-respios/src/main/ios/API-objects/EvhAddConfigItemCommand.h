//
// EvhAddConfigItemCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddConfigItemCommand
//
@interface EvhAddConfigItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* configName;

@property(nonatomic, copy) NSString* configType;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* configProps;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

