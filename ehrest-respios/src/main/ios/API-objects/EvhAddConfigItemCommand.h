//
// EvhAddConfigItemCommand.h
// generated at 2016-04-05 13:45:26 
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

